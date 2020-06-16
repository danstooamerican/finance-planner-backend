package com.financeplanner;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @PostMapping("/add-transaction")
    public int addTransaction(@RequestBody Transaction transaction) {
        System.out.println("Received transaction " + transaction);
        return 42;
    }

}
