package com.financeplanner.datasource.mapper;

import com.financeplanner.domain.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a {@link ResultSet} row to a {@link Transaction}.
 * This implementation expects that all necessary columns to create a {@link com.financeplanner.domain.Category}
 * are available in the {@link ResultSet}.
 */
public class TransactionMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet resultSet, int i) throws SQLException {
        Transaction transaction = new Transaction();

        transaction.setId(resultSet.getInt("id"));
        transaction.setAmount(resultSet.getDouble("amount"));
        transaction.setCategory(new CategoryMapper().mapRow(resultSet, i));
        transaction.setDate(resultSet.getDate("date").toLocalDate());
        transaction.setDescription(resultSet.getString("description"));

        return transaction;
    }

}
