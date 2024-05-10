package com.codegym.finwallet.dto.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private Long id;
    private LocalDate transactionDate;
    private double amount;
    private String description;
    private String fullName;
    private String walletName;
    private String categoryName;
    private String type;
    private String currency;
    private boolean isExpense;
    private boolean isTransfer;
}
