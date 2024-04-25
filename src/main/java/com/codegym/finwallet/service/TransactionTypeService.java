package com.codegym.finwallet.service;

import com.codegym.finwallet.entity.TransactionType;

public interface TransactionTypeService {
    TransactionType addBudgetToTransactionType(Long id,float transactionBudget);
    boolean isBudgetExceeded(TransactionType transactionType, float transactionAmount);
    void alertBudgetExceeded();
}
