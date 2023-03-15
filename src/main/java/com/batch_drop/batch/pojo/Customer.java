package com.batch_drop.batch.pojo;

import jakarta.persistence.*;

@Entity
@Table(name="customer_t")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long account_no;
    private String name;
    private String phoneNo; //phone_no
    private double balance;

    public Long getAccount_no() {
        return account_no;
    }

    public void setAccount_no(Long account_no) {
        this.account_no = account_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Customer(){}

    public Customer(String name, String phoneNo, double balance) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.balance = balance;
    }
}
