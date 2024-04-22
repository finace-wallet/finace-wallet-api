package com.codegym.finwallet.service;


import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WalletService {
    Page<Wallet> findAllByEmail(Pageable pageable);
    Wallet save(WalletRequest request);
    void remove(Long id);
    Wallet findById(Long id);

    CommonResponse editWallet(Long id, WalletRequest walletRequest);
}
