package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.entity.TransactionType;
import com.codegym.finwallet.repository.TransactionTypeRepository;
import com.codegym.finwallet.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {
    private final TransactionTypeRepository transactionTypeRepository;

    @Override
    public TransactionType addBudgetToTransactionType(Long id, double transactionBudget) {
        TransactionType transactionType = transactionTypeRepository.findById(id).orElseThrow(()
        -> new IllegalArgumentException("Invalid transaction type Id: " + id) );
        transactionType.setTransactionBudget(transactionBudget);
        return transactionTypeRepository.save(transactionType);
    }

    @Override
    public boolean isBudgetExceeded(TransactionType transactionType, double transactionAmount) {
        double transactionBudget = transactionType.getTransactionBudget();
       return transactionAmount > transactionBudget;
    }

    @Override
    public void alertBudgetExceeded() {
        System.out.println("Transaction amount exceeds budget");
    }
}
