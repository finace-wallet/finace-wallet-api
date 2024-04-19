package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.WalletDto;
import com.codegym.finwallet.entity.Wallet;

public interface WalletService {
    WalletDto update(WalletDto walletDto);
    void delete(Long id);
}
