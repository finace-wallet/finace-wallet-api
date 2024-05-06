package com.codegym.finwallet.dto.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpendingRequest {
    private double amount;
    private Long userDefType_id;
    private Long wallet_id;
    private String note;
}
