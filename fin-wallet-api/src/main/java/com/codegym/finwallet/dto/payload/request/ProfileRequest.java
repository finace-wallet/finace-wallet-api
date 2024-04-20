package com.codegym.finwallet.dto.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    @NotNull
    private String fullName;
    @NotBlank
    @NotNull
    private String phoneNumber;
    @NotNull
    private String address;
    private String imageUrl;
    @NotNull
    private LocalDate birthDate;
}
