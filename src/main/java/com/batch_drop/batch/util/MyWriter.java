package com.batch_drop.batch.util;

import com.batch_drop.batch.pojo.Transaction;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public class MyWriter implements ItemReader<Transaction> {

    @Autowired
    private DataSource dataSource;

    @Override
    public Transaction read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
