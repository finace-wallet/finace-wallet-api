package com.codegym.finwallet.dto.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionRequest {
    private String walletSenderId;
    private String walletReceiverId;
    private double transactionAmount;
    private LocalDateTime transactionDate;
    private Long transactionCategoryId;
    private String description;
}
