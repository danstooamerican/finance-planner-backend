package com.financeplanner.presentation;

import com.financeplanner.config.security.CurrentUser;
import com.financeplanner.config.security.UserPrincipal;
import com.financeplanner.domain.Category;
import com.financeplanner.domain.CategoryRepository;
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

/**
 * Receives REST calls for interacting with {@link Transaction transactions}.
 */
@RestController
public class TransactionController {

    private final ManageTransactions manageTransactions;

    /**
     * Creates a new {@link TransactionController}.
     *
     * @param manageTransactions the {@link ManageTransactions service} which is used to perform the domain tasks.
     */
    public TransactionController(ManageTransactions manageTransactions) {
        this.manageTransactions = manageTransactions;
    }

    /**
     * Adds a new {@link Transaction transaction}.
     *
     * @param transaction the {@link Transaction transaction} to be added.
     * @param user the currently authenticated {@link UserPrincipal user}.
     * @return the id of the newly added {@link Transaction transaction}.
     */
    @PostMapping("/add-transaction")
    public ResponseEntity<Integer> addTransaction(@RequestBody Transaction transaction, @CurrentUser UserPrincipal user) {
        if (transaction == null) {
            return ResponseEntity.badRequest().body(null);
        }

        final int id = manageTransactions.addTransaction(transaction, user.getId());

        return ResponseEntity.ok(id);
    }

    /**
     * Edits an existing {@link Transaction transaction}.
     *
     * @param transaction the {@link Transaction transaction} to be edited.
     * @param user the currently authenticated {@link UserPrincipal user}.
     * @return whether the {@link Transaction transaction} was edited successfully.
     */
    @PostMapping("/edit-transaction")
    public ResponseEntity<Void> editTransaction(@RequestBody Transaction transaction, @CurrentUser UserPrincipal user) {
        if (transaction == null) {
            return ResponseEntity.badRequest().body(null);
        }

        manageTransactions.editTransaction(transaction, user.getId());

        return ResponseEntity.ok().body(null);
    }

    /**
     * Deletes an existing {@link Transaction transaction}.
     *
     * @param id the id of the {@link Transaction transaction}.
     * @return whether the {@link Transaction} was deleted successfully.
     */
    @PostMapping("/delete-transaction")
    public ResponseEntity<Void> deleteTransaction(@RequestBody int id) {
        manageTransactions.deleteTransaction(id);

        return ResponseEntity.ok().body(null);
    }

    /**
     * Finds all {@link Transaction transactions} belonging to the currently authenticated {@link UserPrincipal user}.
     *
     * @param user the currently authenticated {@link UserPrincipal user}.
     * @return all currently stored {@link Transaction transactions}.
     */
    @GetMapping("/transactions")
    public ResponseEntity<Collection<Transaction>> getAllTransactions(@CurrentUser UserPrincipal user) {
        return ResponseEntity.ok(manageTransactions.getAllTransactions(user.getId()));
    }

}
