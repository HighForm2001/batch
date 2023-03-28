package com.batch_drop.batch.util;

import com.batch_drop.batch.entity.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionMapper_Stored implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransaction_reference(rs.getLong(1));
        transaction.setAmount(rs.getDouble(2));
        transaction.setTransaction_date(((Timestamp) rs.getObject(3)).toLocalDateTime());
        return transaction;
    }
}
