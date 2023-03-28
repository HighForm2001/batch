package com.batch_drop.batch.config;

import com.batch_drop.batch.entity.TestObject;
import com.batch_drop.batch.entity.Transaction;
import com.batch_drop.batch.entity.TransactionReport;
import com.batch_drop.batch.processor.TransactionReportProcessor;
import com.batch_drop.batch.util.TestMapper;
import com.batch_drop.batch.util.TransactionMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.StoredProcedureItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.WritableResource;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Types;
import java.time.LocalDateTime;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    // transaction
    private final String[] transaction_order = new String[] {"transaction_reference","amount","currency","transaction_id","transaction_date"};

    private final String report_header ="Reference Number,Total (RM),Date,Day";//"Reference Number,Total (RM),TXN Number,Date";
    // transactionReport
    private final String[] report_order =
            new String[] {"amount", "amount", "transaction_reference","amount","transaction_date","day"};

    private final String[] testHeader = new String[] {"id","value"};



    @Bean
    public JpaTransactionManager transactionManager(){
        return new JpaTransactionManager();
    }

    @Bean
    public Job transactionToTransactionReport(JobRepository repository, PlatformTransactionManager manager, DataSource dataSource) {
        System.out.println("Job 1 initialized");
        return new JobBuilder("Transaction Report",repository)
                .flow(transactionReportStep(repository,manager,dataSource)).build().build();
    }

    @Bean
    public Step transactionReportStep(JobRepository repository, PlatformTransactionManager manager, DataSource dataSource) {
        return new StepBuilder("testStep",repository).<Transaction, TransactionReport>chunk(100, manager)
                .reader(itemReader(dataSource))
                .processor(transactionReportProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    Job test_job(JobRepository repository,PlatformTransactionManager manager,DataSource dataSource) {
        return new JobBuilder("Job2",repository)
                .flow(testObjectStep(repository,manager,dataSource)).build().build();
    }

    @Bean
    public Step testObjectStep(JobRepository repository, PlatformTransactionManager manager, DataSource dataSource) {
        return new StepBuilder("Job 2 Step",repository)
                .<TestObject,TestObject>chunk(100,manager)
                .reader(testReader(dataSource))
                .writer(testWriter())
                .build();
    }
    @Bean
    Job testStoredProcedureOnTransaction(JobRepository repository,
                                         PlatformTransactionManager manager,
                                         DataSource dataSource){
        return new JobBuilder("Stored Procedure Transaction Report",repository)
                .flow(testStoredProcedureStep(repository,manager,dataSource)).build().build();
    }

    @Bean
    public Step testStoredProcedureStep(JobRepository repository,
                                        PlatformTransactionManager manager,
                                        DataSource dataSource){
        return new StepBuilder("testStep",repository)
                .<Transaction, TransactionReport>chunk(100, manager)
                .reader(synchronizedReader(dataSource))
                .processor(transactionReportProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    Job testStoredProcedureOnTestObject(JobRepository repository,
                                        PlatformTransactionManager manager,
                                        DataSource dataSource){
        return new JobBuilder("Test Stored Procedure with Test Object",repository)
                .flow(testStoredProcedure_Test_object(repository,manager,dataSource))
                .build()
                .build();
    }

    @Bean
    public Step testStoredProcedure_Test_object(JobRepository repository, PlatformTransactionManager manager, DataSource dataSource){
        return new StepBuilder("Test storedProcedure with test_object",repository)
                .<TestObject,TestObject>chunk(100,manager)
                .reader(test_stored_reader(dataSource))
                .writer(testWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<TransactionReport> itemWriter() {
        String time = LocalDateTime.now().toLocalDate()+"_"+
                LocalDateTime.now().toLocalTime().toString().replace(":","_");
        String transaction_file_path = "transaction/transaction_"+time + ".csv";
        WritableResource resource = new PathResource(transaction_file_path);

        BeanWrapperFieldExtractor<TransactionReport> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(report_order);
        fieldExtractor.afterPropertiesSet();


        DelimitedLineAggregator<TransactionReport> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<TransactionReport>()
                .name("transactionWriter")
                .resource(resource)
                .headerCallback(writer -> writer.write(report_header))
                .lineAggregator(lineAggregator)
                .build();
    }
    @Bean
    @StepScope
    public FlatFileItemWriter<TestObject> testWriter(){
        String time = LocalDateTime.now().toLocalDate()+"_"+ LocalDateTime.now().toLocalTime().toString().replace(":","_");
        String test_file_path = "test/test_"+time+".csv";
        WritableResource resource = new PathResource(test_file_path);

        BeanWrapperFieldExtractor<TestObject> extractor = new BeanWrapperFieldExtractor<>();
        String header = String.join(",",testHeader);
        extractor.setNames(testHeader);
        extractor.afterPropertiesSet();

        DelimitedLineAggregator<TestObject> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(extractor);

        return new FlatFileItemWriterBuilder<TestObject>()
                .name("TestObject writer")
                .headerCallback(writer -> writer.write(header))
                .resource(resource)
                .lineAggregator(lineAggregator)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Transaction> itemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Transaction>()
                .dataSource(dataSource)
                .name("transactionReader")
                .sql("SELECT t.transaction_reference, t.amount, t.currency, t.transaction_id ," +
                        " t.transaction_date, t.from_acc_id, t.to_acc_id FROM transaction_t t")
                .rowMapper(new TransactionMapper())
                .build();
    }
    @Bean
    @StepScope
    public JdbcCursorItemReader<TestObject> testReader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<TestObject>()
                .dataSource(dataSource)
                .name("testObject Reader")
                .sql("SELECT id,value from test_object_t")
                .rowMapper(new TestMapper())
                .build();
    }


    @Bean
    public StoredProcedureItemReader<TestObject> test_stored_reader(DataSource dataSource){
        StoredProcedureItemReader<TestObject> reader = new StoredProcedureItemReader<>();
        reader.setDataSource(dataSource);
        reader.setProcedureName("test_object");
        reader.setRowMapper(new TestMapper());
        return reader;
    }

    @Bean
    public StoredProcedureItemReader<Transaction> storedProReader(DataSource dataSource){

        SqlParameter[] parameterList = new SqlParameter[]
                {new SqlParameter("intCreator", Types.INTEGER)};
        StoredProcedureItemReader<Transaction> reader = new StoredProcedureItemReader<>();
        reader.setDataSource(dataSource);
        reader.setProcedureName("test_function3");
        reader.setParameters(parameterList);
        reader.setRowMapper(new TransactionMapper());
        reader.setPreparedStatementSetter(statementSetter());
        return reader;
    }

    public PreparedStatementSetter statementSetter(){
        return ps -> ps.setInt(1,3);
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<Transaction> synchronizedReader(DataSource dataSource){
        StoredProcedureItemReader<Transaction> spReader = storedProReader(dataSource);
        SynchronizedItemStreamReader<Transaction> syncReader = new SynchronizedItemStreamReader<>();
        syncReader.setDelegate(spReader);
        return syncReader;
    }

    @Bean
    public TransactionReportProcessor transactionReportProcessor(){
        return new TransactionReportProcessor();
    }

}
