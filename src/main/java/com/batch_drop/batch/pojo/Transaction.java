package com.batch_drop.batch.pojo;

import com.batch_drop.batch.enums.Currency;
import jakarta.persistence.*;


@Entity
@Table(name = "transaction_t")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transaction_reference;
    private double amount;
    private Currency currency;
    private int transaction_id;

    private long from_acc_id;
    private long to_acc_id;

    public Transaction(double amount, Currency currency, long from_acc_id, long to_acc_id) {
        this.amount = amount;
        this.currency = currency;
        this.from_acc_id = from_acc_id;
        this.to_acc_id = to_acc_id;
    }

    public Transaction(){}

    public long getTransaction_reference() {
        return transaction_reference;
    }

    public void setTransaction_reference(Long transaction_reference) {
        this.transaction_reference = transaction_reference;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public long getFrom_acc_id() {
        return from_acc_id;
    }

    public void setFrom_acc_id(long from_acc_id) {
        this.from_acc_id = from_acc_id;
    }

    public long getTo_acc_id() {
        return to_acc_id;
    }

    public void setTo_acc_id(long to_acc_id) {
        this.to_acc_id = to_acc_id;
    }
}
