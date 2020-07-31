package com.financeplanner.datasource;

import com.financeplanner.config.security.AuthProvider;
import com.financeplanner.domain.Category;
import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JDBCStatisticsRepositoryTest {

    private static final String CLEAR_USERS_QUERY = "delete from user";
    private static final String CLEAR_TRANSACTIONS_QUERY = "delete from transaction";
    private static final String CLEAR_CATEGORIES_QUERY = "delete from category";

    private static final String DESCRIPTION = "description";

    private static final Category CATEGORY = new Category(0, "name",
            new Category.IconData(42, "font_family", "font_package"));

    private final User user = new User(0, "name", "email", "image_url",
            AuthProvider.facebook, "provider_id");

    private final User otherUser = new User(0, "name2", "email2", "image_url2",
            AuthProvider.google, "provider_id2");

    private final JDBCTransactionRepository jdbcTransactionRepository;

    private final JDBCStatisticsRepository jdbcStatisticsRepository;
    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public JDBCStatisticsRepositoryTest(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcStatisticsRepository = new JDBCStatisticsRepository(dataSource);

        JDBCUserRepository jdbcUserRepository = new JDBCUserRepository(dataSource);
        jdbcUserRepository.save(user);
        jdbcUserRepository.save(otherUser);

        JDBCCategoryRepository jdbcCategoryRepository = new JDBCCategoryRepository(dataSource);
        jdbcCategoryRepository.save(CATEGORY, user.getId());

        jdbcTransactionRepository = new JDBCTransactionRepository(dataSource);

    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update(CLEAR_TRANSACTIONS_QUERY);
    }

    @AfterAll
    static void clearDatabase() {
        jdbcTemplate.update(CLEAR_TRANSACTIONS_QUERY);
        jdbcTemplate.update(CLEAR_CATEGORIES_QUERY);
        jdbcTemplate.update(CLEAR_USERS_QUERY);
    }

    @Test
    void getCurrentMonthBalance_multipleUser_getsBalanceForUser() {
        final int amtTransactions = 10;
        final double minAmount = -50;
        final double maxAmount = 50;

        double expectedBalance = 0;
        for (int i = 0; i < amtTransactions; i++) {
            double amount = Math.round(Math.random() * 100.0) / 100.0 * 2 * maxAmount - minAmount;

            jdbcTransactionRepository.save(getUnsavedTransaction(amount, LocalDate.now()), user.getId());

            expectedBalance += amount;
        }

        jdbcTransactionRepository.save(getUnsavedTransaction(1, LocalDate.now().minusMonths(1)), user.getId());
        jdbcTransactionRepository.save(getUnsavedTransaction(-2, LocalDate.now().plusMonths(1)), user.getId());

        jdbcTransactionRepository.save(getUnsavedTransaction(5, LocalDate.now()), otherUser.getId());
        jdbcTransactionRepository.save(getUnsavedTransaction(-6, LocalDate.now()), otherUser.getId());

        double balance = jdbcStatisticsRepository.getCurrentMonthBalance(user.getId());

        assertEquals(expectedBalance, balance);
    }

    private Transaction getUnsavedTransaction(double amount, LocalDate date) {
        return new Transaction(amount, CATEGORY, DESCRIPTION, date);
    }

}
