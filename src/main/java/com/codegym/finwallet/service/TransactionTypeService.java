package com.codegym.finwallet.service;

import com.codegym.finwallet.entity.TransactionType;

public interface TransactionTypeService {
    TransactionType addBudgetToTransactionType(Long id,double transactionBudget);
    boolean isBudgetExceeded(TransactionType transactionType, double transactionAmount);
    void alertBudgetExceeded();
}
