package com.financeplanner.datasource;

import com.financeplanner.domain.StatisticsRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Repository
public class JDBCStatisticsRepository implements StatisticsRepository {

    public static final String createCurrentMonthBalanceView = "create or replace view CurrentMonthBalance as " +
            "select user.id as user_id, coalesce(sum(amount), 0) as balance from user " +
            "left join transaction on user.id = transaction.user_id " +
            "and date_format(transaction.date, '%Y-%m-01') = date_format(now(), '%Y-%m-01') " +
            "group by user.id";

    private static final String getCurrentMonthBalanceForUser =
            "select balance from CurrentMonthBalance where user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Creates a new {@link JDBCStatisticsRepository}.
     *
     * @param dataSource the {@link DataSource data source} which is used to access the data.
     */
    public JDBCStatisticsRepository(@NotNull DataSource dataSource) {
        Objects.requireNonNull(dataSource);

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public double getCurrentMonthBalance(int userId) {
        Double balance = jdbcTemplate.queryForObject(getCurrentMonthBalanceForUser, Double.class, userId);

        if (balance != null) {
            return balance;
        }

        return 0;
    }

}
