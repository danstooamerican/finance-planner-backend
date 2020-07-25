package com.financeplanner.presentation;

import com.financeplanner.config.security.CurrentUser;
import com.financeplanner.config.security.UserPrincipal;
import com.financeplanner.domain.Transaction;
import com.financeplanner.domain.User;
import com.financeplanner.domain.services.ManageTransactions;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<Integer> addTransaction(@RequestBody Transaction transaction, @CurrentUser UserPrincipal user) {
        if (transaction == null) {
            return ResponseEntity.badRequest().body(null);
        }

        final int id = manageTransactions.addTransaction(transaction, user.getId());

        return ResponseEntity.ok(id);
    }

    @PostMapping("/edit-transaction")
    public ResponseEntity<Void> editTransaction(@RequestBody Transaction transaction, @CurrentUser UserPrincipal user) {
        if (transaction == null) {
            return ResponseEntity.badRequest().body(null);
        }

        manageTransactions.editTransaction(transaction, user.getId());

        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/delete-transaction")
    public ResponseEntity<Void> deleteTransaction(@RequestBody int id) {
        manageTransactions.deleteTransaction(id);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/transactions")
    public ResponseEntity<Collection<Transaction>> getAllTransactions(@CurrentUser UserPrincipal user) {
        return ResponseEntity.ok(manageTransactions.getAllTransactions(user.getId()));
    }

}
