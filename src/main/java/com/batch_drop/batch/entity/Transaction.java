package com.batch_drop.batch.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "transaction_t")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transaction_reference;
    private double amount;
    private String currency;
    @Column(name = "transaction_id")
//    private int transactionID;
    private int transaction_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="from_acc_id")
    private Customer creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="to_acc_id")
    private Customer receiver;

    @Column(name = "transaction_date")
    private LocalDateTime transaction_date;

    public void setTransaction_date(){
        this.transaction_date = LocalDateTime.now();
    }
    public void setTransaction_date(LocalDateTime time){
        this.transaction_date = time;
    }
    public LocalDate getDate(){
        return transaction_date.toLocalDate();
    }





}
