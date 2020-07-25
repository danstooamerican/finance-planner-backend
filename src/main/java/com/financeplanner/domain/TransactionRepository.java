package com.financeplanner.domain;

import java.util.Collection;
import org.springframework.stereotype.Repository;

/**
 * Access for stored {@link Transaction transactions}.
 */
@Repository
public interface TransactionRepository {

    /**
     * Stores a given {@link Transaction transaction}. If the transaction is already stored all parameters
     * are updated.
     * @param transaction the {@link Transaction transaction} to store.
     * @return the id of the {@link Transaction transaction}
     */
    int save(Transaction transaction);

    /**
     * @return all currently stored {@link Transaction transactions}.
     */
    Collection<Transaction> findAllTransactions();

    /**
     * Deletes a stored {@link Transaction transaction} with the given id.
     * @param id the id of the {@link Transaction transaction} which is deleted.
     */
    void delete(int id);

}
