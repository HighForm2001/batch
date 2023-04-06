package com.batch_drop.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfig {


    @Bean
    public Job transactionToTransactionReport(JobRepository repository,
                                              @Qualifier("transactionReportStep") Step step) {
        return new JobBuilder("Transaction Report",repository)
                .flow(step).build().build();
    }

    @Bean
    Job testJob(JobRepository repository, @Qualifier("testObjectStep") Step step) {
        return new JobBuilder("Job2",repository)
                .flow(step).build().build();
    }

    @Bean
    Job testStoredProcedureOnTransaction(JobRepository repository, @Qualifier("testStoredProcedureStep") Step step){
        return new JobBuilder("Stored Procedure Transaction Report",repository)
                .flow(step).build().build();
    }

    @Bean
    Job testStoredProcedureOnTestObject(JobRepository repository, @Qualifier("testStoredProcedureTestObject") Step step){
        return new JobBuilder("Test Stored Procedure with Test Object",repository)
                .flow(step)
                .build()
                .build();
    }

    @Bean
    Job transactionRepositoryJob(JobRepository jobRepository, @Qualifier("transactionRepositoryStep") Step step){

        return new JobBuilder("Repository Job",jobRepository)
                .flow(step)
                .build()
                .build();
    }

    @Bean
    public Job testRepoJob(JobRepository jobRepository,@Qualifier("testRepoStep") Step step){
        System.out.println("Launching testRepoJob Every 1 s");
        return new JobBuilder("testRepoJob",jobRepository)
                .flow(step)
                .build().build();
    }

    @Bean
    public Job partitionJob(JobRepository jobRepository, @Qualifier("partitionStep") Step step){
        System.out.println("Launching partition job. Every 3 s");
        return new JobBuilder("Partition Job",jobRepository)
                .flow(step).build().build();
    }

}
