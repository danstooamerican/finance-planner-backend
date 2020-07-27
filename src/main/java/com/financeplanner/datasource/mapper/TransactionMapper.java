package com.financeplanner.datasource.mapper;

import com.financeplanner.domain.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a {@link ResultSet} row to a {@link Transaction}.
 * This implementation expects that all necessary columns to create a {@link com.financeplanner.domain.Category}
 * are available in the {@link ResultSet}.
 */
public class TransactionMapper extends BaseMapper<Transaction> {

    private final String categoryIdentifier;

    public TransactionMapper(String categoryIdentifier) {
        super();

        this.categoryIdentifier = categoryIdentifier;
    }

    @Override
    public Transaction mapRow(ResultSet resultSet, int i) throws SQLException {
        Transaction transaction = new Transaction();

        transaction.setId(resultSet.getInt(getColumnName("id")));
        transaction.setAmount(resultSet.getDouble(getColumnName("amount")));
        transaction.setCategory(new CategoryMapper(categoryIdentifier).mapRow(resultSet, i));
        transaction.setDate(resultSet.getDate(getColumnName("date")).toLocalDate());
        transaction.setDescription(resultSet.getString(getColumnName("description")));

        return transaction;
    }

}
