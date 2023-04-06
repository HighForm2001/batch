package com.batch_drop.batch.components;

import com.batch_drop.batch.entity.Transaction;
import com.batch_drop.batch.entity.TransactionReport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TransactionReportProcessor
        implements ItemProcessor<Transaction, TransactionReport> {
    @Override
    public TransactionReport process(Transaction item) {
        TransactionReport report = new TransactionReport();
        int dayCode_int = item.getDate().getDayOfWeek().getValue();
        String dayCode = switch (dayCode_int) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            default -> "Sunday";
        };
        report.setDay(dayCode);
        report.setTransaction_reference(item.getTransaction_reference());
        report.setTransaction_date(item.getDate());
        report.setAmount("RM " + String.format("%.2f",item.getAmount()));
        return report;
    }
}
