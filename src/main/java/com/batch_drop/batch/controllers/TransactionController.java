package com.batch_drop.batch.controllers;

import com.batch_drop.batch.dao.CustomerRepository;
import com.batch_drop.batch.dao.TransactionRepository;
import com.batch_drop.batch.pojo.Customer;
import com.batch_drop.batch.pojo.Transaction;
import com.batch_drop.batch.service.CsvExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionRepository repository;
    private final CustomerRepository customerRepository;
    private final CsvExportService csvExportService;
    public TransactionController(TransactionRepository repository, CustomerRepository customerRepository, CsvExportService csvExportService){
        this.repository = repository;
        this.customerRepository = customerRepository;
        this.csvExportService = csvExportService;
    }

    @RequestMapping
    public void getAllTransactionsInCsv(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"/home/transactions.csv\"");
        csvExportService.writeTransactionsToCsv(servletResponse.getWriter());
    }


}
