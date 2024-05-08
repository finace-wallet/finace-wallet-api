package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.WalletConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.OwnerShip;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.entity.WalletOwnership;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.OwnerShipRepository;
import com.codegym.finwallet.repository.WalletOwnershipRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.WalletShareService;
import com.codegym.finwallet.util.AuthUserExtractor;
import com.codegym.finwallet.util.BuildCommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletShareServiceServiceImpl implements WalletShareService {
    private final WalletOwnershipRepository walletOwnershipRepository;
    private final AppUserRepository appUserRepository;
    private final WalletRepository walletRepository;
    private final OwnerShipRepository ownerShipRepository;
    private final BuildCommonResponse commonResponse;
    private final AuthUserExtractor userExtractor;
    @Override
    public CommonResponse walletShare(String shareEmail, String accessLevel, Long shareWalletId) {
        String currentEmail = userExtractor.getUsernameFromAuth();
        if (shareEmail.equals(currentEmail)) {
            return commonResponse.builResponse(null, WalletConstant.SHARE_WALLET_FAIL, HttpStatus.BAD_REQUEST);
        }
        Optional<AppUser> appUserShareOptional = appUserRepository.findAppUserByEmail(shareEmail);
        if (appUserShareOptional.isEmpty()) {
            return commonResponse.builResponse(null, WalletConstant.INVALID_RECIPIENT_EMAIL, HttpStatus.BAD_REQUEST);
        }
        Optional<WalletOwnership> recipientOwnership = walletOwnershipRepository.findByAppUserEmailAndWalletIdAndIsDeleteFalse(shareEmail, shareWalletId);
        if (recipientOwnership.isEmpty()) {
            CommonResponse response = findOrCreateOwnership(shareEmail, accessLevel, shareWalletId);
            if (response != null) {
                return response;
            } else {
                return commonResponse.builResponse(null, WalletConstant.SHARE_WALLET_FAIL, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return commonResponse.builResponse(null, WalletConstant.SHARE_WALLET_SUCCESSFUL, HttpStatus.CREATED);
    }

    private CommonResponse findOrCreateOwnership(String shareEmail, String accessLevel, Long shareWalletId) {
        WalletOwnership walletOwnership = new WalletOwnership();
        AppUser receiver = appUserRepository.findByEmail(shareEmail);
        walletOwnership.setAppUser(receiver);
        Wallet wallet = walletRepository.findById(shareWalletId).orElseThrow();
        walletOwnership.setWallet(wallet);

        Optional<OwnerShip> ownerShipOptional = ownerShipRepository.findByName(accessLevel);
        if (ownerShipOptional.isPresent()) {
            walletOwnership.setOwnerShip(ownerShipOptional.get());
            walletOwnership.setDelete(false);
            walletOwnershipRepository.save(walletOwnership);
            return commonResponse.builResponse(null, WalletConstant.SHARE_WALLET_SUCCESSFUL, HttpStatus.CREATED);
        }

        return commonResponse.builResponse(null, WalletConstant.SHARE_WALLET_FAIL, HttpStatus.OK);
    }
}
