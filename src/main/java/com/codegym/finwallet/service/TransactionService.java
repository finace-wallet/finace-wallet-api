package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;

public interface TransactionService {
    CommonResponse create(TransactionRequest request, Long id);
    CommonResponse getAllTransactions(Long walletId);
}
