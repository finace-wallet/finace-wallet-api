package com.codegym.finwallet.dto.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpendingResponse {
    private String userDefTypeName;
    private String walletName;
    private String note;
    private LocalDateTime localDateTime;
    private double amount;
}
