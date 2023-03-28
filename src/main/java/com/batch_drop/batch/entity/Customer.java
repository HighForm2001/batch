package com.batch_drop.batch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="customer_t")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_no")
    private Long accountNo;
    private String name;
    private String phoneNo; //phone_no
    private double balance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private List<Transaction> transactionsMade;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiver")
    private List<Transaction> transactionsReceive;


}
