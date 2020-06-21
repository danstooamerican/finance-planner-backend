package com.financeplanner.domain.services;

import com.financeplanner.domain.Category;
import com.financeplanner.domain.CategoryRepository;
import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.TransactionRepository;
import java.util.Collection;
import org.springframework.stereotype.Service;

/**
 * Service class which manages {@link Transaction} objects.
 */
@Service
public class ManageTransactions {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Creates a new {@link ManageTransactions}.
     * @param transactionRepository used to access {@link Transaction} objects.
     * @param categoryRepository used to access {@link Category} objects.
     */
    public ManageTransactions(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Adds a new {@link Transaction transaction}.
     * @param transaction the {@link Transaction transactionn} to save.
     * @return the id of the new {@link Transaction transaction}.
     */
    public int addTransaction(Transaction transaction) {
        return saveTransaction(transaction);
    }

    /**
     * Edits an existing {@link Transaction transaction}
     * @param transaction the new {@link Transaction transaction}.
     */
    public void editTransaction(Transaction transaction) {
        saveTransaction(transaction);
    }

    private int saveTransaction(Transaction transaction) {
        categoryRepository.saveCategory(transaction.getCategory());
         return transactionRepository.saveTransaction(transaction);
    }

    /**
     * Deletes an existing {@link Transaction transaction} with the given id.
     * @param id the id of the {@link Transaction transaction}.
     */
    public void deleteTransaction(int id) {
        transactionRepository.deleteTransaction(id);
    }

    /**
     * @return all added {@link Transaction transactions}.
     */
    public Collection<Transaction> getAllTransactions() {
        return transactionRepository.fetchAllTransactions();
    }

}
