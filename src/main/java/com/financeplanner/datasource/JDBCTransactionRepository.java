package com.financeplanner.datasource;

import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;

@Repository
public class JDBCTransactionRepository implements TransactionRepository {

    private static final String findAllTransactionForUser =
            "select * from transaction where user_id = ?";

    private static final String deleteTransactionQuery =
            "delete from transaction where id = ?";

    private static final String insertOrUpdateTransactionQuery =
            "insert into transaction (id, amount, category_id, date, description, user_id) " +
                    "values (:id, :amount, :category_id, :date, :description, :user_id) on DUPLICATE key " +
                    "update amount = :amount, category_id = :category_id, date = :date, description = :description," +
                    "user_id = :user_id";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JDBCTransactionRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public int save(Transaction transaction, Long userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", transaction.getId())
                .addValue("amount", transaction.getAmount())
                .addValue("category_id", transaction.getCategory().getId())
                .addValue("date", transaction.getDate())
                .addValue("description", transaction.getDescription())
                .addValue("user_id", userId);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(insertOrUpdateTransactionQuery, namedParameters, keyHolder, new String[] { "id" });

        Number id = keyHolder.getKey();
        if (id != null) {
            return id.intValue();
        }

        return transaction.getId();
    }

    @Override
    public Collection<Transaction> findAllTransactions(Long userId) {
        return jdbcTemplate.queryForList(findAllTransactionForUser, Transaction.class, userId);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(deleteTransactionQuery, id);
    }

}
