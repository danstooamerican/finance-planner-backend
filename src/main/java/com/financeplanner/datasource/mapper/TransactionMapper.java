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

    private final String identifier;
    private final String categoryIdentifier;

    public TransactionMapper(String identifier, String categoryIdentifier) {
        if (identifier.length() > 0) {
            this.identifier = identifier + ".";
            this.categoryIdentifier = categoryIdentifier + ".";
        } else {
            this.identifier = "";
            this.categoryIdentifier = "";
        }
    }

    public TransactionMapper(String categoryIdentifier) {
        this.identifier = "";
        this.categoryIdentifier = categoryIdentifier;
    }

    public TransactionMapper() {
        this.identifier = "";
        this.categoryIdentifier = "";
    }

    @Override
    public Transaction mapRow(ResultSet resultSet, int i) throws SQLException {
        Transaction transaction = new Transaction();

        transaction.setId(resultSet.getInt(identifier + "id"));
        transaction.setAmount(resultSet.getDouble(identifier + "amount"));
        transaction.setCategory(new CategoryMapper(categoryIdentifier).mapRow(resultSet, i));
        transaction.setDate(resultSet.getDate(identifier + "date").toLocalDate());
        transaction.setDescription(resultSet.getString(identifier + "description"));

        return transaction;
    }

}
