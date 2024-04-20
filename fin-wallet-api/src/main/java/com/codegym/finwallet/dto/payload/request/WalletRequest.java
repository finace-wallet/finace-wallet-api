package com.codegym.finwallet.dto.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletRequest {
    private String icon;
    private double amount;
    private String currentType;
    private String description;
    private String name;
}
