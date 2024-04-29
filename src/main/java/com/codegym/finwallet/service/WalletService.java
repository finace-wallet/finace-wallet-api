package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WalletService {
    Page<Wallet> findAllByEmail(Pageable pageable);
    CommonResponse createWallet(WalletRequest request);
    CommonResponse deleteWallet(Long id);
    Wallet findById(Long id);
    CommonResponse editWallet(WalletRequest walletRequest,Long id);
//    CommonResponse transferMoney(TransferMoneyRequest transferMoneyRequest);
    CommonResponse addMoneyToWallet(Long walletId, double amount);
}
