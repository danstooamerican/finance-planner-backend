package com.financeplanner.presentation;

import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.services.ManageTransactions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final ManageTransactions manageTransactions;

    public TransactionController(ManageTransactions manageTransactions) {
        this.manageTransactions = manageTransactions;
    }

    @PostMapping("/add-transaction")
    public ResponseEntity<Integer> addTransaction(@RequestBody Transaction transaction) {
        if (transaction == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        final int id = manageTransactions.addTransaction(transaction);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(id);
    }

}
