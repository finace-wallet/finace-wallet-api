package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;
import com.codegym.finwallet.entity.Transaction;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    CommonResponse saveTransaction(TransactionRequest request, Long walletId);
    CommonResponse findAllTransactionsByWalletId(Long walletId, Pageable pageable);
    CommonResponse findAllTransactionsByCategory(Long walletId, Long transactionCategoryId, Pageable pageable);
    CommonResponse deleteTransaction(Long transactionId);
    CommonResponse transferMoney(TransferMoneyRequest request, Long walletId);
    CommonResponse editTransaction(TransactionRequest request, Long walletId, Long transactionId);
    CommonResponse getAllTransactionsAndAmount(Long categoryId,Long walletID);
    List<Transaction> getAllTransactionsPeriod(Long walletId, LocalDate startDate, LocalDate endDate, String timeType);
}
