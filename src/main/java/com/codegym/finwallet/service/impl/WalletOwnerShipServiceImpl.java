package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.WalletOwnershipRepository;
import com.codegym.finwallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WalletOwnerShipServiceImpl {
    private final WalletOwnershipRepository walletOwnershipRepository;
    private final AppUserRepository appUserRepository;
    private final WalletRepository walletRepository;

}
