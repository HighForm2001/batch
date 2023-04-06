package com.batch_drop.batch.components;

import com.batch_drop.batch.entity.TestObject;
import com.batch_drop.batch.entity.Transaction;
import com.batch_drop.batch.entity.TransactionReport;
import com.batch_drop.batch.repositories.TransactionRepository;
import com.batch_drop.batch.util.RecordPartitioner;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.StoredProcedureItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
public class StepComponent {

    @Autowired
    FlatFileItemWriter<TransactionReport> reportWriter;

    @Autowired
    FlatFileItemWriter<Transaction> transactionWriter;

    @Autowired
    FlatFileItemWriter<TestObject> testWriter;

    @Autowired
    MultiResourceItemWriter<Transaction> multiResourceItemWriter;

    @Autowired
    JdbcCursorItemReader<Transaction> itemReader;

    @Autowired
    JdbcCursorItemReader<TestObject> testReader;

    @Autowired
    StoredProcedureItemReader<TestObject> test_stored_reader;


    @Autowired
    SynchronizedItemStreamReader<Transaction> synchronizedReader;

    @Autowired
    RepositoryItemReader<Transaction> repositoryItemReader;

    @Autowired
    RepositoryItemReader<TestObject> testRepoReader;

    @Autowired
    TransactionRepository transactionRepository;

    @Bean
    public Step transactionRepositoryStep(JobRepository jobRepository, PlatformTransactionManager manager){
        return new StepBuilder("RepositoryReader",jobRepository)
                .<Transaction,Transaction>chunk(1,manager)
                .reader(repositoryItemReader)
                .writer(transactionWriter)
                .build();
    }
    @Bean
    @Qualifier(value = "testRepoStep")
    public Step testRepoStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("testRepoStep", jobRepository)
                .<TestObject,TestObject> chunk(100,platformTransactionManager)
                .reader(testRepoReader)
                .writer(testWriter)
                .build();
    }
    @Bean
    @Qualifier(value = "partitionStep")
    public Step partitionStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager){
//        return new StepBuilder("Partition Step",jobRepository)
//                .partitioner("slaveStep",partitioner())
//                .step(transactionRepositoryStep(jobRepository,platformTransactionManager))
//                .gridSize(2)
//                .build();
        return new StepBuilder("Partition Step",jobRepository)
                .<Transaction,Transaction> chunk(3,platformTransactionManager)
                .reader(repositoryItemReader)
                .writer(multiResourceItemWriter)
                .build();
    }
    @Bean
    public Partitioner partitioner(){
        return new RecordPartitioner(transactionRepository);
    }
    @Bean
    public Step testStoredProcedureTestObject(JobRepository repository, PlatformTransactionManager manager, DataSource dataSource){

        return new StepBuilder("Test storedProcedure with test_object",repository)
                .<TestObject,TestObject>chunk(100,manager)
                .reader(test_stored_reader)
                .writer(testWriter)
                .build();
    }
    @Bean
    public Step testStoredProcedureStep(JobRepository repository,
                                        PlatformTransactionManager manager,
                                        DataSource dataSource){
        return new StepBuilder("testStep",repository)
                .<Transaction, TransactionReport>chunk(100, manager)
                .reader(synchronizedReader)
                //.processor(transactionReportProcessor())
                .writer(reportWriter)
                .build();
    }
    @Bean
    public Step testObjectStep(JobRepository repository, PlatformTransactionManager manager, DataSource dataSource) {
        return new StepBuilder("Job 2 Step",repository)
                .<TestObject,TestObject>chunk(100,manager)
                .reader(testReader)
                .writer(testWriter)
                .build();
    }
    @Bean
    public Step transactionReportStep(JobRepository repository, PlatformTransactionManager manager, DataSource dataSource) {
        return new StepBuilder("testStep",repository).<Transaction, TransactionReport>chunk(100, manager)
                .reader(itemReader)
                //.processor(transactionReportProcessor())
                .writer(reportWriter)
                .build();
    }
}
