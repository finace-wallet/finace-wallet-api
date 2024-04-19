package com.codegym.finwallet.dto.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "Email can't empty or have a blank")
    @NotNull
    private String email;
    @NotBlank
    @NotNull(message = "Password can't empty or have a blank")
    private String password;
}
