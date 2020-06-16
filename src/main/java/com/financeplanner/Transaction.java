package com.financeplanner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Transaction {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private LocalDate date;
    private String description;
    private String category;
    private double amount;
}
