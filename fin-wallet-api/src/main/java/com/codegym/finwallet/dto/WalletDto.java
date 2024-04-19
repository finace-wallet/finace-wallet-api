package com.codegym.finwallet.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String icon;
    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private float amount;
    @NotNull
    @NotBlank
    private String currentType;
    @NotNull
    @NotBlank
    private String description;
    @NotNull

    private Long user_id;
}
