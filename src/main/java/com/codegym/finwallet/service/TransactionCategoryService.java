package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionCategoryRequest;
import com.codegym.finwallet.entity.TransactionCategory;

import java.util.List;

public interface TransactionCategoryService {
    CommonResponse createTransactionCategory(TransactionCategoryRequest request);
    List<TransactionCategory> findAllTransactionCategoryByEmail();
}