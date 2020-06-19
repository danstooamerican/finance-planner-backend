package com.financeplanner.domain.services;

import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.TransactionRepository;
import java.util.Collection;
import org.springframework.stereotype.Service;

@Service
public class ManageTransactions {

    private final TransactionRepository transactionRepository;

    public ManageTransactions(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public int addTransaction(Transaction transaction) {
        return transactionRepository.saveTransaction(transaction);
    }


    public void editTransaction(Transaction transaction) {
        transactionRepository.saveTransaction(transaction);
    }

    public void deleteTransaction(int id) {
        transactionRepository.deleteTransaction(id);
    }

    public Collection<Transaction> getAllTransactions() {
        return transactionRepository.fetchAllTransactions();
    }

}
