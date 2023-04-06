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

@SpringBootApplication
@EnableScheduling
//@EnableBatchProcessing
public class BatchApplication {



	@Autowired
	JobLauncher launcher;

	@Autowired
	Job transactionToTransactionReport; // TransactionReport job

	@Autowired
	Job testJob; // Test job

	@Autowired
	Job testStoredProcedureOnTransaction; // Stored Procedure

	@Autowired
	Job testStoredProcedureOnTestObject;

	@Autowired
	Job transactionRepositoryJob;

	@Autowired
	Job partitionJob;

	@Autowired
	Job testRepoJob;

	public static void main(String[] args) {
		System.setProperty("Password","1234");
		SpringApplication.run(BatchApplication.class, args);
	}

//	@Scheduled(cron = "* * * * * *")
//	public void performTransactionMapIntoTransactionReport() throws Exception{
//		JobParameters param = new JobParametersBuilder().addString("Job ID",String.valueOf(System.currentTimeMillis()))
//				.toJobParameters();
//		launcher.run(transactionToTransactionReport, param);
//		System.out.println("Run transactionToTransactionReport on " + LocalDateTime.now());
//	}

//	@Scheduled(cron = "*/10 * * * * *")
//	public void performTestObjectRun() throws Exception{
//		JobParameters parameters = new JobParametersBuilder()
//				.addString("Job 2 ID",String.valueOf(System.currentTimeMillis()))
//				.toJobParameters();
//		launcher.run(test_job,parameters);
//		System.out.println("Run test_job on " + LocalDateTime.now());
//	}

//	@Scheduled(cron = "*/5 * * * * *")
//	public void performStoredProcedureOnTransaction() throws Exception{
//		JobParameters parameters = new JobParametersBuilder()
//				.addString("Job 3 ID",String.valueOf(System.currentTimeMillis()))
//				.toJobParameters();
//		launcher.run(testStoredProcedureOnTransaction,parameters);
//		System.out.println("Run testStoredProcedureOnTransaction on " + LocalDateTime.now());
//	}

//	@Scheduled(cron = "* * * * * *")
//	public void performStoredProcedureOnTestObject() throws Exception{
//		JobParameters parameters = new JobParametersBuilder().addString("Job 4 ID",String.valueOf(System.currentTimeMillis()))
//				.toJobParameters();
//		launcher.run(testStoredProcedureOnTestObject,parameters);
//		System.out.println("Run testStoredProcedureOnTestObject job on " + LocalDateTime.now());
//	}

//	@Scheduled(cron = "*/5 * * * * *")
//	public void	performRepositoryReader() throws Exception{
//		JobParameters parameters = new JobParametersBuilder()
//				.addString("Repository Reader ID: ",String.valueOf(System.currentTimeMillis()))
//				.toJobParameters();
//		launcher.run(transactionRepositoryJob,parameters);
//
//	}

//	@Scheduled(cron = "*/1 * * * * *")
//	public void	performTestRepoJob() throws Exception{
//		JobParameters parameters = new JobParametersBuilder()
//				.addString("Test Repo ID: ",String.valueOf(System.currentTimeMillis()))
//				.toJobParameters();
//		launcher.run(testRepoJob,parameters);
//
//	}

	@Scheduled(cron = "*/3 * * * * *")
	public void	performPartitionJob() throws Exception{
		JobParameters parameters = new JobParametersBuilder()
				.addString("Partition Job ID: ",String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		launcher.run(partitionJob,parameters);

	}
}
