package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.entity.WalletOwnership;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.WalletOwnershipRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.WalletShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletShareServiceServiceImpl implements WalletShareService {
    private final WalletOwnershipRepository walletOwnershipRepository;
    private final AppUserRepository appUserRepository;
    private final WalletRepository walletRepository;
    @Override
    public CommonResponse walletShare(String shareEmail, String accessLevel, Long shareWalletId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        if(shareEmail.equals(currentEmail)) {
            return CommonResponse.builder()
                    .data(null)
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Can't use your own email")
                    .build();
        }

        Optional<AppUser> appUserShareOptional = appUserRepository.findAppUserByEmail(shareEmail);
        if (appUserShareOptional.isPresent()) {
            // Implement logic to share the wallet with the provided email and access level
            // ...
            Optional<WalletOwnership> recipientOwnership = walletOwnershipRepository.findByAppUserEmailAndWalletIdAndIsDeleteFalse(shareEmail, shareWalletId);
            if (recipientOwnership.isPresent()) {
                // Recipient already has ownership, handle this case (e.g., send a message)
            } else {
                // Implement logic to share the wallet with the provided email and access level
                // ...
                WalletOwnership walletOwnership = new WalletOwnership();
                AppUser receiver = appUserRepository.findByEmail(shareEmail);
                walletOwnership.setAppUser(receiver);
                Wallet wallet = walletRepository.findById(shareWalletId).get();
                walletOwnership.setWallet(wallet);
                walletOwnership.setOwnership(accessLevel);
                walletOwnership.setDelete(false);
                System.out.println("Wallet ownership" + walletOwnership);
                walletOwnershipRepository.save(walletOwnership);
                return CommonResponse.builder()
                        .data(null)
                        .status(HttpStatus.CREATED)
                        .message("You've shared the wallet")
                        .build();
            }
        } else {
            // Handle case where the shareEmail doesn't exist
            return CommonResponse.builder()
                    .data(null)
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Invalid recipient email.")
                    .build();
        }

        return CommonResponse.builder()
                .data(null)
                .status(HttpStatus.OK)
                .message("Can't use your own email")
                .build();
    }
}
