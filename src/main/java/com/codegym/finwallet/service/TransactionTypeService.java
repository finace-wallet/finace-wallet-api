package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionTypeRequest;
import com.codegym.finwallet.entity.TransactionType;

public interface TransactionTypeService {
    CommonResponse updateBudgetToTransactionType(Long id, float transactionBudget);
    boolean isBudgetExceeded(TransactionTypeRequest transactionTypeRequest, float transactionAmount);
    void alertBudgetExceeded();
}
