package com.codegym.finwallet.dto.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionRequest {
    private String senderName;
    private String recipientName;
    private Float transactionAmount;
    private String description;
}
