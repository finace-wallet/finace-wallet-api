package com.codegym.finwallet.dto.payload.response;

import com.codegym.finwallet.entity.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private Long id;
    private LocalDateTime transactionDate;
    private double amount;
    private String description;
    private String fullName;
    private String walletName;
    private String categoryName;
    private String type;
}
