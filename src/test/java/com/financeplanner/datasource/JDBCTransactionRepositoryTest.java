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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JDBCTransactionRepositoryTest {

    private static final String CLEAR_USERS_QUERY = "delete from user";
    private static final String CLEAR_TRANSACTIONS_QUERY = "delete from transaction";
    private static final String CLEAR_CATEGORIES_QUERY = "delete from category";

    private static final int ID_NOT_SAVED = 0;
    private static final double AMOUNT = 10.51;
    private static final String DESCRIPTION = "description";
    private static final LocalDate DATE = LocalDate.now();

    private static final Category CATEGORY = new Category(0, "name",
            new Category.IconData(42, "font_family", "font_package"));

    private final User user = new User(0L, "name", "email", "image_url",
            AuthProvider.facebook, "provider_id");

    private final JDBCTransactionRepository jdbcTransactionRepository;
    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public JDBCTransactionRepositoryTest(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcTransactionRepository = new JDBCTransactionRepository(dataSource);

        JDBCUserRepository jdbcUserRepository = new JDBCUserRepository(dataSource);
        jdbcUserRepository.save(user);

        JDBCCategoryRepository jdbcCategoryRepository = new JDBCCategoryRepository(dataSource);
        jdbcCategoryRepository.save(CATEGORY, user.getId());
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
    void delete_notExists() {
        assertDoesNotThrow(() -> jdbcTransactionRepository.delete(ID_NOT_SAVED));
    }

    @Test
    void delete_exists() {
        int id = jdbcTransactionRepository.save(getUnsavedTransaction(), user.getId());

        jdbcTransactionRepository.delete(id);

        Collection<Transaction> transactions = jdbcTransactionRepository.findAllTransactions(user.getId());

        assertTrue(transactions.isEmpty());
    }

    @Test
    void save_updatedTransactionId() {
        Transaction transaction = getUnsavedTransaction();

        int id = jdbcTransactionRepository.save(transaction, user.getId());

        assertEquals(id, transaction.getId());
        assertNotEquals(ID_NOT_SAVED, id);
    }

    @Test
    void save_transactionSaved() {
        Transaction transaction = getUnsavedTransaction();

        jdbcTransactionRepository.save(transaction, user.getId());

        Collection<Transaction> transactions = jdbcTransactionRepository.findAllTransactions(user.getId());

        assertEquals(1, transactions.size());
        Transaction actual = transactions.stream().findFirst().get();

        assertTransactionEquals(transaction, actual);
    }

    @Test
    void save_updatesTransaction_idDoesntChange() {
        Transaction transaction = getUnsavedTransaction();
        final double expectedAmount = transaction.getAmount() + 1;

        int id = jdbcTransactionRepository.save(transaction, user.getId());


        transaction.setAmount(expectedAmount);
        jdbcTransactionRepository.save(transaction, user.getId());

        assertEquals(id, transaction.getId());

        Collection<Transaction> transactions = jdbcTransactionRepository.findAllTransactions(user.getId());

        assertEquals(1, transactions.size());
        Transaction actual = transactions.stream().findFirst().get();

        assertTransactionEquals(transaction, actual);
    }

    @Test
    void findAllTransactions_noResults() {
        Collection<Transaction> transactions = jdbcTransactionRepository.findAllTransactions(user.getId());

        assertTrue(transactions.isEmpty());
    }

    @Test
    void findAllTransactions_multipleResults() {
        int id1 = jdbcTransactionRepository.save(getUnsavedTransaction(), user.getId());
        int id2 = jdbcTransactionRepository.save(getUnsavedTransaction(), user.getId());

        Collection<Transaction> transactions = jdbcTransactionRepository.findAllTransactions(user.getId());

        assertEquals(2, transactions.size());

        List<Transaction> transactionList = transactions.stream()
                .sorted(Comparator.comparingLong(Transaction::getId))
                .collect(Collectors.toList());

        assertEquals(id1, transactionList.get(0).getId());
        assertEquals(id2, transactionList.get(1).getId());
    }

    private Transaction getUnsavedTransaction() {
        return new Transaction(AMOUNT, CATEGORY, DESCRIPTION, DATE);
    }

    private void assertTransactionEquals(Transaction expected, Transaction actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getDescription(), actual.getDescription());

        assertCategoryEquals(expected.getCategory(), actual.getCategory());
    }

    private void assertCategoryEquals(Category expected, Category actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getIcon(), actual.getIcon());
        assertEquals(expected.getId(), actual.getId());
    }

}
