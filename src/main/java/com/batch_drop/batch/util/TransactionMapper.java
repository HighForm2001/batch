package com.batch_drop.batch.util;

import com.batch_drop.batch.enums.Currency;
import com.batch_drop.batch.pojo.Transaction;
import org.springframework.jdbc.core.RowMapper;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransaction_id(rs.getInt("transaction_id"));
        transaction.setTransaction_reference(rs.getLong("transaction_reference"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setCurrency(Currency.valueOf("GBP"));
        transaction.setTo_acc_id(rs.getLong("to_acc_id"));
        transaction.setFrom_acc_id(rs.getLong("from_acc_id"));
        return transaction;
    }
}
