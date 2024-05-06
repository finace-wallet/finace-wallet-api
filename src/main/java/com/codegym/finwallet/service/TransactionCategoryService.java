package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionCategoryRequest;
import com.codegym.finwallet.entity.TransactionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TransactionCategoryService {
    CommonResponse createTransactionCategory(TransactionCategoryRequest request);
    List<TransactionCategory> findAllTransactionCategoryByEmail();
}
