package com.financeplanner.datasource;

import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.TransactionRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CRUDTransactionRepository extends CrudRepository<Transaction, Integer>, TransactionRepository {

    default int saveTransaction(Transaction transaction) {
        this.save(transaction);

        return transaction.getId();
    }

}
