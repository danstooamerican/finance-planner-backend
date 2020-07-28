package com.financeplanner.domain.services;

import com.financeplanner.domain.Category;
import com.financeplanner.domain.CategoryRepository;
import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ManageTransactionsTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Category category;

    @Mock
    private Transaction transaction;

    @Mock
    private Collection<Transaction> transactions;

    private ManageTransactions manageTransactions;

    private final int userId = 0;
    private final int transactionId = 0;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        this.manageTransactions = new ManageTransactions(transactionRepository, categoryRepository);

        when(transaction.getCategory()).thenReturn(category);
        when(transactionRepository.findAllTransactions(userId)).thenReturn(transactions);
    }

    @Test
    void ManageTransactions_noError() {
        assertDoesNotThrow(() -> {
            new ManageTransactions(transactionRepository, categoryRepository);
        });

        assertThrows(NullPointerException.class, () -> {
            new ManageTransactions(null, categoryRepository);
        });

        assertThrows(NullPointerException.class, () -> {
            new ManageTransactions(transactionRepository, null);
        });

        assertThrows(NullPointerException.class, () -> {
            new ManageTransactions(null, null);
        });
    }

    @Test
    void addTransaction_nullTransaction_throwsNullPointer() {
        assertThrows(NullPointerException.class, () -> {
            manageTransactions.addTransaction(null, userId);
        });
    }

    @Test
    void addTransaction_savesTransaction() {
        manageTransactions.addTransaction(transaction, userId);

        verify(categoryRepository).save(category, userId);
        verify(transactionRepository).save(transaction, userId);
    }

    @Test
    void editTransaction_nullTransaction() {
        assertThrows(NullPointerException.class, () -> {
            manageTransactions.editTransaction(null, userId);
        });
    }

    @Test
    void editTransaction_savesTransaction() {
        manageTransactions.editTransaction(transaction, userId);

        verify(categoryRepository).save(category, userId);
        verify(transactionRepository).save(transaction, userId);
    }

    @Test
    void deleteTransaction_deletesTransaction() {
        manageTransactions.deleteTransaction(transactionId);

        verify(transactionRepository).delete(transactionId);
    }

    @Test
    void getAllTransactions_getsUserTransaction() {
        Collection<Transaction> result = manageTransactions.getAllTransactions(userId);

        assertEquals(transactions, result);
    }
}
