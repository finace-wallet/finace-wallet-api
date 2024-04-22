package com.codegym.finwallet.dto.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    @NotNull
    @Min(0)
    private double amount;
    @NotBlank
    @NotNull
    private String currentType;
    @NotBlank
    @NotNull
    private String description;
    @NotBlank
    @NotNull
    private String name;
}
