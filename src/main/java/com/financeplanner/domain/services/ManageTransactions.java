package com.financeplanner.domain.services;

import com.financeplanner.domain.CategoryRepository;
import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.TransactionRepository;
import java.util.Collection;
import org.springframework.stereotype.Service;

@Service
public class ManageTransactions {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public ManageTransactions(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public int addTransaction(Transaction transaction) {
        categoryRepository.saveCategory(transaction.getCategory());
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
