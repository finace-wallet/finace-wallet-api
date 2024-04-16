package com.codegym.finwallet.service;

import com.codegym.finwallet.entity.Wallet;

public interface WalletService {
    Wallet update(Wallet wallet);
    void delete(Long id);
}
