package com.financeplanner.domain.services;

import com.financeplanner.domain.*;

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
     *
     * @param transactionRepository used to access {@link Transaction} objects.
     * @param categoryRepository used to access {@link Category} objects.
     */
    public ManageTransactions(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Adds a new {@link Transaction transaction}.
     *
     * @param transaction the {@link Transaction transaction} to save.
     * @param userId the id of the {@link User user} which belongs to the {@link Transaction transaction}.
     * @return the id of the new {@link Transaction transaction}.
     */
    public int addTransaction(Transaction transaction, Long userId) {
        return saveTransaction(transaction, userId);
    }

    /**
     * Edits an existing {@link Transaction transaction}.
     *
     * @param transaction the new {@link Transaction transaction}.
     * @param userId the id of the {@link User user} which belongs to the {@link Transaction transaction}.
     */
    public void editTransaction(Transaction transaction, Long userId) {
        saveTransaction(transaction, userId);
    }

    private int saveTransaction(Transaction transaction, Long userId) {
        categoryRepository.save(transaction.getCategory(), userId);

        return transactionRepository.save(transaction, userId);
    }

    /**
     * Deletes an existing {@link Transaction transaction} with the given id.
     *
     * @param id the id of the {@link Transaction transaction}.
     */
    public void deleteTransaction(int id) {
        transactionRepository.delete(id);
    }

    /**
     * Finds all stored {@link Transaction transactions} belonging to the given {@link User user}.
     *
     * @param userId the id of the {@link User user} which belongs to the {@link Transaction transactions}.
     * @return all added {@link Transaction transactions}.
     */
    public Collection<Transaction> getAllTransactions(Long userId) {
        return transactionRepository.findAllTransactions(userId);
    }

}
