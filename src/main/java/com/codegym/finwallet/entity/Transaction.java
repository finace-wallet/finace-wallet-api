package com.codegym.finwallet.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime transactionDate;
    private double amount;
    private String description;
    private boolean isDelete;
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "transaction_category_id")
    @JsonBackReference
    private TransactionCategory transactionCategory;
}
