package com.codegym.finwallet.service;

import com.codegym.finwallet.entity.TransactionType;

public interface TransactionTypeService {
    boolean isBudgetExceeded(TransactionType transactionType, double transactionAmount);
    void alertBudgetExceeded();
}
