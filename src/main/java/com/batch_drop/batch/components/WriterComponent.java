package com.batch_drop.batch.components;

import com.batch_drop.batch.entity.TestObject;
import com.batch_drop.batch.entity.Transaction;
import com.batch_drop.batch.entity.TransactionReport;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.ResourceSuffixCreator;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class WriterComponent {

    // transaction
    private final String[] transaction_order = new String[] {"transaction_reference","amount","currency","transaction_id","transaction_date"};

    private final String report_header ="Reference Number,Total (RM),Date,Day";//"Reference Number,Total (RM),TXN Number,Date";
    // transactionReport
    private final String[] report_order =
            new String[] {"amount", "amount", "transaction_reference","amount","transaction_date","day"};

    private final String[] testHeader = new String[] {"id","value"};
    @Bean
    @StepScope
    public FlatFileItemWriter<TransactionReport> reportWriter() {
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
    public FlatFileItemWriter<Transaction> transactionWriter(@Value("#{stepExecutionContext['outputFilePath']}")String outputFilePath){
        String time = LocalDateTime.now().toLocalDate()+"_"+
                LocalDateTime.now().toLocalTime().toString().replace(":","_");
        String transaction_file_path = "transaction/transaction_"+time + (outputFilePath!=null?outputFilePath:"") +  ".csv";
        WritableResource resource = new PathResource(transaction_file_path);

        BeanWrapperFieldExtractor<Transaction> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(transaction_order);
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<Transaction> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<Transaction>()
                .name("Repository Writer")
                .resource(resource)
                .headerCallback(writer -> writer.write(String.join(",",transaction_order)))
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
    public MultiResourceItemWriter<Transaction> multiResourceItemWriter() throws IOException {
        MultiResourceItemWriter<Transaction> writer = new MultiResourceItemWriter<>();
        String transaction_file_path = "transaction/";

        WritableResource resource = new PathResource(transaction_file_path);
        if (!resource.exists())
            resource.getFile().mkdirs();
        writer.setItemCountLimitPerResource(3); // must set
        writer.setDelegate(individualReportWriter());
        writer.setResourceSuffixCreator(index -> "/transaction-" +LocalDateTime.now().toString().replace(":","_") +"-"+ index + ".csv");
        writer.setResource(resource);
        return writer;
    }

    @Bean
    public FlatFileItemWriter<Transaction> individualReportWriter(){
        BeanWrapperFieldExtractor<Transaction> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(transaction_order);
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<Transaction> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<Transaction>()
                .name("transactionWriter")
                .headerCallback(writer -> writer.write(String.join(",",transaction_order)))
                .lineAggregator(lineAggregator)
                .build();
    }

}
