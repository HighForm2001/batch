package com.batch_drop.batch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionReport {
    private Long transaction_reference;
    private LocalDate transaction_date;
    private String amount;
    private String day;
}
