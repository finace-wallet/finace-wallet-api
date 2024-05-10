package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.entity.Transaction;

public interface TransactionService {
    CommonResponse saveTransaction(TransactionRequest request, Long walletId);
}
