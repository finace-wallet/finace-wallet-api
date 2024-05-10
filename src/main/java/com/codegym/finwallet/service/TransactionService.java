package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.entity.Transaction;
import com.codegym.finwallet.entity.TransactionCategory;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    CommonResponse saveTransaction(TransactionRequest request, Long walletId);
    CommonResponse findAllTransactionsByWalletId(Long walletId, Pageable pageable);
    CommonResponse findAllTransactionsByCategory(Long walletId, Long transactionCategoryId, Pageable pageable);
}
