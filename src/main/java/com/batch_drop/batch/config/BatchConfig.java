package com.batch_drop.batch.config;

import com.batch_drop.batch.pojo.Transaction;
import com.batch_drop.batch.util.MyWriter;
import com.batch_drop.batch.util.TransactionMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    private JobBuilder jobs;

    @Autowired
    private StepBuilder steps;

    @Autowired
    private DataSource dataSource;

    @Bean
    public JobBuilder jobBuilder() {
        return new JobBuilder("testing"); // Job Repository as second parameter
    }
    @Bean
    public StepBuilder stepBuilder(){
        return new StepBuilder("testStep"); // Job Repository as second parameter
    }

    @Bean
    public Job sampleJob(Step step){
        return jobs.start(step).build();
    }

    @Bean
    public Step stepOne() throws Exception {

        return steps.<Transaction,Transaction>chunk(100)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<Transaction> itemWriter() throws Exception{
        WritableResource resource = new PathResource("resources/transaction.csv");
        BeanWrapperFieldExtractor<Transaction> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"transaction_reference","amount","currency","transaction_id","from_acc_id","to_acc_id"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<Transaction> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<Transaction>()
                .name("transactionWriter")
                .resource(resource)
                .lineAggregator(lineAggregator)
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Transaction> itemReader() throws Exception {
        return new JdbcCursorItemReaderBuilder<Transaction>()
                .dataSource(this.dataSource)
                .name("transactionReader")
                .sql("SELECT * FROM transaction_t")
                .rowMapper(new TransactionMapper())
                .build();
    }
}
