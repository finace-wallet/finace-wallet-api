package com.codegym.finwallet.dto.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferMoneyRequest {
    private Long sourceWalletId;
    private Long destinationWalletId;
    private String destinationEmail;
    private float amount;
}
