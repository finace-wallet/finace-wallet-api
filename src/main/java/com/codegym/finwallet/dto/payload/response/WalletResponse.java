package com.codegym.finwallet.dto.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletResponse {
    private Long id;
    private String name;
    private double amount;
    private String currentType;
    private String description;
    private boolean isDelete;
    private String ownership;
    private Set<TransactionCategoryResponse> transactionCategory;
}
