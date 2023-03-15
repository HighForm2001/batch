package com.batch_drop.batch.service;

import com.batch_drop.batch.dao.TransactionRepository;
import com.batch_drop.batch.pojo.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;


@Service
public class CsvExportService {
    private static final Logger log = LoggerFactory.getLogger(CsvExportService.class);
    private TransactionRepository repository = null;
    public CsvExportService(TransactionRepository repository){
        this.repository = repository;
    }

    public void writeTransactionsToCsv(Writer writer){
        List<Transaction> transactions = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)){
            csvPrinter.printRecord("transaction_reference","amount","currency"
                    ,"transaction_id","from_acc_id","to_acc_id");
            for (Transaction t:transactions){
                csvPrinter.printRecord(t.getTransaction_reference(),t.getAmount(),
                        t.getCurrency(),t.getTransaction_id(),t.getFrom_acc_id(),t.getTo_acc_id());
            }
        }catch(IOException e){
            log.error("Error while writing csv ", e);
        }
    }


}
