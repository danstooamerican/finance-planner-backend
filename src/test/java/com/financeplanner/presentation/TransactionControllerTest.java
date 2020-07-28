package com.financeplanner.presentation;

import com.financeplanner.config.security.UserPrincipal;
import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.services.ManageTransactions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionControllerTest {

    private static final int USER_ID = 0;
    private static final int TRANSACTION_ID = 42;

    @Mock
    private ManageTransactions manageTransactions;

    @Mock
    private UserPrincipal userPrincipal;

    @Mock
    private Transaction transaction;

    @Mock
    private Collection<Transaction> transactions;

    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        when(userPrincipal.getId()).thenReturn(USER_ID);
        when(manageTransactions.addTransaction(transaction, USER_ID)).thenReturn(TRANSACTION_ID);
        when(manageTransactions.getAllTransactions(USER_ID)).thenReturn(transactions);

        this.transactionController = new TransactionController(manageTransactions);
    }

    @Test
    void addTransaction_validTransaction_addsTransaction() {
        ResponseEntity<Integer> result = transactionController.addTransaction(transaction, userPrincipal);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Integer id = result.getBody();

        assertNotNull(id);
        assertEquals(TRANSACTION_ID, id.intValue());

        verify(manageTransactions).addTransaction(transaction, USER_ID);
    }

    @Test
    void addTransaction_nullTransaction_triggersBadRequest() {
        ResponseEntity<Integer> result = transactionController.addTransaction(null, userPrincipal);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        Integer id = result.getBody();

        assertNull(id);
    }

    @Test
    void addTransaction_nullUserPrincipal_triggersBadRequest() {
        ResponseEntity<Integer> result = transactionController.addTransaction(transaction, null);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        Integer id = result.getBody();

        assertNull(id);
    }

    @Test
    void editTransaction_validTransaction_editsTransaction() {
        ResponseEntity<Void> result = transactionController.editTransaction(transaction, userPrincipal);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Void body = result.getBody();

        assertNull(body);

        verify(manageTransactions).editTransaction(transaction, USER_ID);
    }

    @Test
    void editTransaction_nullTransaction_triggersBadRequest() {
        ResponseEntity<Void> result = transactionController.editTransaction(null, userPrincipal);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        Void body = result.getBody();

        assertNull(body);
    }

    @Test
    void editTransaction_nullUserPrincipal_triggersBadRequest() {
        ResponseEntity<Void> result = transactionController.editTransaction(transaction, null);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        Void body = result.getBody();

        assertNull(body);
    }

    @Test
    void deleteTransaction_deletesTransaction() {
        ResponseEntity<Void> result = transactionController.deleteTransaction(TRANSACTION_ID);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Void body = result.getBody();

        assertNull(body);

        verify(manageTransactions).deleteTransaction(TRANSACTION_ID);
    }

    @Test
    void getAllTransactions_validUser_findsAllTransactions() {
        ResponseEntity<Collection<Transaction>> result = transactionController.getAllTransactions(userPrincipal);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Collection<Transaction> body = result.getBody();

        assertNotNull(body);
        assertEquals(transactions, body);
    }

    @Test
    void getAllTransactions_nullUser_triggersBadRequest() {
        ResponseEntity<Collection<Transaction>> result = transactionController.getAllTransactions(null);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

        Collection<Transaction> body = result.getBody();

        assertNotNull(body);
        assertTrue(body.isEmpty());
    }
}
