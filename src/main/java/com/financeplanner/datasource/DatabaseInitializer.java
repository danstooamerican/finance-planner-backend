package com.financeplanner.datasource;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

/**
 * Helper class to make sure that database tables are created in the correct order.
 */
public class DatabaseInitializer {

    /**
     * Creates all necessary database tables to run the application.
     *
     * @param dataSource used to perform commands on the loaded database.
     */
    public static void initializeDatabase(@NotNull DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(JDBCUserRepository.createUsersTable);
        jdbcTemplate.update(JDBCCategoryRepository.createCategoriesTable);
        jdbcTemplate.update(JDBCTransactionRepository.createTransactionsTable);
    }

}
