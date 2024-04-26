package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionTypeRequest;
import com.codegym.finwallet.entity.TransactionType;

public interface TransactionTypeService {
    CommonResponse updateBudgetInTransactionType(Long id, float transactionBudget);
    CommonResponse addBudgetToTransactionType(Long id, float additionalBudget);
    boolean isBudgetExceeded(TransactionTypeRequest transactionTypeRequest, float transactionAmount);
    void alertBudgetExceeded();
}
