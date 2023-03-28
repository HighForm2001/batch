package com.batch_drop.batch.util;

import com.batch_drop.batch.entity.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TransactionMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction();
//        transaction.setTransaction_id(rs.getInt(4));
        transaction.setTransaction_reference(rs.getLong(1));
        transaction.setAmount(rs.getDouble(2));
        transaction.setCurrency(rs.getString(3));
//        transaction.setTransaction_date(((Timestamp)rs
//        .getObject(5)).toLocalDateTime()); // JDBC reder
        transaction.setTransaction_date(((Timestamp)rs.getObject(7)).toLocalDateTime()); // Stored Procedure Reader
//        transaction.setTransactionID(rs.getInt("transactionID"));
        transaction.setTransaction_id(rs.getInt("transaction_id"));
//        transaction.setTransaction_reference(rs.getLong("transaction_reference"));
//        transaction.setAmount(rs.getDouble("amount"));
//        transaction.setCurrency(rs.getString("currency"));
//        transaction.setTransaction_date(((Timestamp)rs.getObject("transaction_date"))
//        .toLocalDateTime());
//        transaction.setReceiver((Customer)rs.getObject("receiver"));
//        transaction.setCreator((Customer) rs.getObject("creator"));
        return transaction;
    }
}
