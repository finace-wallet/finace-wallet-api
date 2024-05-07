package com.codegym.finwallet.dto.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletResponse {
    private String name;
    private double amount;
    private String currentType;
    private String description;
    private double transactionBudget;
}
