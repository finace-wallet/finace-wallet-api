package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionCategoryRequest;
import com.codegym.finwallet.entity.TransactionCategory;
import com.codegym.finwallet.entity.Wallet;

public interface TransactionCategoryService {
    CommonResponse createTransactionCategory(TransactionCategoryRequest request, Long walletId);
}
