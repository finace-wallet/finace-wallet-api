package com.codegym.finwallet.dto.payload.response;

import com.codegym.finwallet.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String fullName;
    private String email;
    private List<RoleResponse> roles;
}
