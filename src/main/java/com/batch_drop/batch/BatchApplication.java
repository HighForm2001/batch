package com.batch_drop.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
//@EnableBatchProcessing
public class BatchApplication {

	@Autowired
	JobLauncher launcher;

	@Autowired
	Job transactionToTransactionReport; // TransactionReport job

	@Autowired
	Job test_job; // Test job

	@Autowired
	Job testStoredProcedureOnTransaction; // Stored Procedure

	@Autowired
	Job testStoredProcedureOnTestObject;

	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

//	@Scheduled(cron = "* * * * * *")
//	public void performTransactionMapIntoTransactionReport() throws Exception{
//		JobParameters param = new JobParametersBuilder().addString("Job ID",String.valueOf(System.currentTimeMillis()))
//				.toJobParameters();
//		launcher.run(transactionToTransactionReport, param);
//		System.out.println("Run transactionToTransactionReport on " + LocalDateTime.now());
//	}

	@Scheduled(cron = "*/10 * * * * *")
	public void performTestObjectRun() throws Exception{
		JobParameters parameters = new JobParametersBuilder()
				.addString("Job 2 ID",String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		launcher.run(test_job,parameters);
		System.out.println("Run test_job on " + LocalDateTime.now());
	}

	@Scheduled(cron = "*/5 * * * * *")
	public void performStoredProcedureOnTransaction() throws Exception{
		JobParameters parameters = new JobParametersBuilder()
				.addString("Job 3 ID",String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		launcher.run(testStoredProcedureOnTransaction,parameters);
		System.out.println("Run testStoredProcedureOnTransaction on " + LocalDateTime.now());
	}

//	@Scheduled(cron = "* * * * * *")
//	public void performStoredProcedureOnTestObject() throws Exception{
//		JobParameters parameters = new JobParametersBuilder().addString("Job 4 ID",String.valueOf(System.currentTimeMillis()))
//				.toJobParameters();
//		launcher.run(testStoredProcedureOnTestObject,parameters);
//		System.out.println("Run testStoredProcedureOnTestObject job on " + LocalDateTime.now());
//	}
}
