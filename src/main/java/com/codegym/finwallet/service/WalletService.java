package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WalletService {
    CommonResponse findAllByEmail(Pageable pageable);
    CommonResponse findWalletByViewer(Pageable pageable);
    CommonResponse findWalletsByEmailAndOwner(Pageable pageable);
    CommonResponse findWalletsByCoOwner(Pageable pageable);
    Page<Wallet> findAllRecipientByEmail(Pageable pageable, String email);
    CommonResponse createWallet(WalletRequest request);
    CommonResponse deleteWallet(Long id);
    Wallet findById(Long id);
    CommonResponse editWallet(WalletRequest walletRequest,Long id);
    CommonResponse addMoneyToWallet(Long walletId, double amount);
}
