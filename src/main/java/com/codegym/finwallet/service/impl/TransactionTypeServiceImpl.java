package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionTypeRequest;
import com.codegym.finwallet.entity.TransactionType;
import com.codegym.finwallet.repository.TransactionTypeRepository;
import com.codegym.finwallet.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.codegym.finwallet.constant.WalletConstant;

@Service
@RequiredArgsConstructor
public class TransactionTypeServiceImpl implements TransactionTypeService {
    private final TransactionTypeRepository transactionTypeRepository;

    @Override
    public CommonResponse updateBudgetInTransactionType(Long id, float transactionBudget) {
        if (transactionBudget < 0) {
            return CommonResponse.builder()
                    .data(null)
                    .message("Transaction budget cannot be less than 0.")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        TransactionType transactionType = transactionTypeRepository.findById(id).orElseThrow(()
        -> new IllegalArgumentException("Invalid transaction type Id: " + id) );
        transactionType.setTransactionBudget(transactionBudget);
        transactionTypeRepository.save(transactionType);
        return CommonResponse.builder()
                .data(transactionType)
                .message(WalletConstant.UPDATE_BUDGET_TRANSACTION)
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public CommonResponse addBudgetToTransactionType(Long id, float additionalBudget) {
        if (additionalBudget < 0) {
            return CommonResponse.builder()
                    .data(null)
                    .message("Additional budget cannot be less than 0.")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        TransactionType transactionType = transactionTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction type Id: " + id));
        float transactionBudget = transactionType.getTransactionBudget();
        float newBudget = transactionBudget + additionalBudget;
        transactionType.setTransactionBudget(newBudget);
        transactionTypeRepository.save(transactionType);
        return CommonResponse.builder()
                .data(transactionType)
                .message(WalletConstant.ADD_BUDGET_TRANSACTION)
                .status(HttpStatus.OK)
                .build();
    }


    @Override
    public boolean isBudgetExceeded(TransactionTypeRequest transactionTypeRequest, float transactionAmount) {
       float transactionBudget = transactionTypeRequest.getTransactionBudget();
       return transactionAmount > transactionBudget;
    }

    @Override
    public void alertBudgetExceeded() {
        System.out.println("Transaction amount exceeds budget");
    }
}
