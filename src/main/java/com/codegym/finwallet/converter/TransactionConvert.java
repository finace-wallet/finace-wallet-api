package com.codegym.finwallet.converter;

import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.entity.Transaction;
import com.codegym.finwallet.repository.TransactionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionConvert {
    private final TransactionCategoryRepository transactionCategoryRepository;

    public Transaction convertRequestToTransaction(TransactionRequest request, Transaction transaction) {
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setTransactionCategory(transactionCategoryRepository.findById(request.getTransactionCategoryId()).orElse(null));
        return transaction;
    }
}
