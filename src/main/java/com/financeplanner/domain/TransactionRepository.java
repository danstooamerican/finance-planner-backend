package com.financeplanner.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository {

    int saveTransaction(Transaction transaction);

}
