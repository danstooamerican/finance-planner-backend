package com.financeplanner.datasource;

import com.financeplanner.datasource.mapper.TransactionMapper;
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
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link TransactionRepository} which
 * uses JDBC and MySQL to access stored {@link Transaction transactions}.
 */
@Repository
public class JDBCTransactionRepository implements TransactionRepository {

    public static final String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transaction (" +
            "  id int NOT NULL AUTO_INCREMENT," +
            "  amount decimal(10,2) NOT NULL," +
            "  description varchar(1000) NOT NULL," +
            "  date date NOT NULL," +
            "  category_id int NOT NULL," +
            "  user_id int NOT NULL," +
            "  PRIMARY KEY (id)," +
            "  FOREIGN KEY (user_id) REFERENCES user (id)," +
            "  FOREIGN KEY (category_id) REFERENCES category (id))";

    private static final String findAllTransactionsForUser =
            "select * from transaction " +
                    "join category c on transaction.category_id = c.id and transaction.user_id = c.user_id " +
                    "where transaction.user_id = ?";

    private static final String deleteTransactionQuery =
            "delete from transaction where id = ?";

    private static final String insertOrUpdateTransactionQuery =
            "insert into transaction (id, amount, category_id, date, description, user_id) " +
                    "values (:id, :amount, :category_id, :date, :description, :user_id) on DUPLICATE key " +
                    "update amount = :amount, category_id = :category_id, date = :date, description = :description," +
                    "user_id = :user_id";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Creates a new {@link JDBCTransactionRepository}.
     *
     * @param dataSource the {@link DataSource data source} which is used to access the data.
     */
    public JDBCTransactionRepository(@NotNull DataSource dataSource) {
        Objects.requireNonNull(dataSource);

        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public int save(@NotNull Transaction transaction, int userId) {
        Objects.requireNonNull(transaction);

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id", transaction.getId())
                .addValue("amount", transaction.getAmount())
                .addValue("category_id", transaction.getCategory().getId())
                .addValue("date", transaction.getDate())
                .addValue("description", transaction.getDescription())
                .addValue("user_id", userId);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(insertOrUpdateTransactionQuery, namedParameters, keyHolder, new String[] { "id" });

        List<Map<String, Object>> keyList = keyHolder.getKeyList();

        if (keyList.size() == 1) {
            Number id = (Number) keyList.get(0).get("GENERATED_KEY");

            if (id != null) {
                final int transactionId = id.intValue();
                transaction.setId(transactionId);

                return transactionId;
            }
        }

        return transaction.getId();
    }

    @Override
    public Collection<Transaction> findAllTransactions(int userId) {
        return jdbcTemplate.query(findAllTransactionsForUser, new TransactionMapper("c"), userId);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(deleteTransactionQuery, id);
    }

}
