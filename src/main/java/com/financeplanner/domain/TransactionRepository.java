package com.financeplanner.domain;

import java.util.Collection;
import org.springframework.stereotype.Repository;

/**
 * Access for stored {@link Transaction transactions}.
 */
@Repository
public interface TransactionRepository {

    /**
     * Stores a given {@link Transaction transaction} for the given {@link User user}.
     * If the transaction is already stored all parameters are updated.
     *
     * @param transaction the {@link Transaction transaction} to store.
     * @param userId the id of the {@link User user} which belongs to the transaction.
     * @return the id of the {@link Transaction transaction}
     */
    int save(Transaction transaction, Long userId);

    /**
     * Finds all currently stored {@link Transaction transactions} which belong to the {@link User user}.
     *
     * @param userId the id of the {@link User user}.
     * @return all {@link Transaction transactions}.
     */
    Collection<Transaction> findAllTransactions(Long userId);

    /**
     * Deletes a stored {@link Transaction transaction} with the given id.
     *
     * @param id the id of the {@link Transaction transaction} which is deleted.
     */
    void delete(int id);

}
