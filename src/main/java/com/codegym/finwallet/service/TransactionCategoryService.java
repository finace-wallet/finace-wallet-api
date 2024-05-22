package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionCategoryRequest;
import com.codegym.finwallet.entity.Wallet;
import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;

public interface TransactionCategoryService {
    CommonResponse createTransactionCategory(TransactionCategoryRequest request, Long walletId);
    CommonResponse getAllCategoryTypeIncome(Long walletId);
    CommonResponse getAllCategoryTypeExpense(Long walletId);
    CommonResponse getAllCategory(Long walletId);
    CommonResponse createBudget(Long walletId, Long categoryId, double budget);
}
