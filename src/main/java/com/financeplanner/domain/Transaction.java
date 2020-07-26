package com.financeplanner.domain;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a transaction which was created by an {@link User user}.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Transaction {

    private int id;

    private LocalDate date;
    private String description;
    private Category category;
    private double amount;

    public Transaction(double amount, Category category, String description, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }
}
