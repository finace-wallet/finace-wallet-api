package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;

public interface WalletShareService {
    CommonResponse walletShare(String shareEmail, String accessLevel, Long shareWalletId);
}
