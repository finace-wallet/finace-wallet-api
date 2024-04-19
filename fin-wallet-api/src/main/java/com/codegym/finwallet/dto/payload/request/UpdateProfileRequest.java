package com.codegym.finwallet.dto.payload.request;

import com.codegym.finwallet.entity.AppUser;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
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
