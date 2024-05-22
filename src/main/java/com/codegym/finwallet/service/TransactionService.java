package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.context.Context;

public interface TransactionService {
    CommonResponse saveTransaction(TransactionRequest request, Long walletId);

    void sendBudgetAlertEmails(String email, Context context) throws MessagingException;

    CommonResponse findAllTransactionsByWalletId(Long walletId, Pageable pageable);
    CommonResponse findAllTransactionsByCategory(Long walletId, Long transactionCategoryId, Pageable pageable);
    CommonResponse deleteTransaction(Long transactionId);
    CommonResponse transferMoney(TransferMoneyRequest request, Long walletId);
    CommonResponse editTransaction(TransactionRequest request, Long walletId, Long transactionId);
    CommonResponse getAllTransactionsAndAmount(Long categoryId,Long walletID);
}
