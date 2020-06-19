package com.financeplanner.domain;

import java.util.Collection;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository {

    int saveTransaction(Transaction transaction);

    Collection<Transaction> fetchAllTransactions();

    void deleteTransaction(int id);

}
