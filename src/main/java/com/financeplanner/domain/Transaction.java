package com.financeplanner.domain;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class Transaction {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalDate date;
    private String description;
    private String category;
    private double amount;

    public Transaction(double amount, String category, String description, LocalDate date) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }
}
