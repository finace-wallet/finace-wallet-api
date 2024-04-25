package com.codegym.finwallet.controller;

import com.codegym.finwallet.entity.TransactionType;
import com.codegym.finwallet.service.TransactionTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactionTypes")
@RequiredArgsConstructor
public class TransactionTypeController {
    private final TransactionTypeService transactionTypeService;

    @PostMapping("/add-budget/{id}")
    public ResponseEntity<TransactionType> addBudgetToTransactionType(@PathVariable Long id,
                                                                      @RequestParam float transactionBudget) {
        TransactionType updatedTransactionType = transactionTypeService.addBudgetToTransactionType(id, transactionBudget);
        return new ResponseEntity<>(updatedTransactionType, HttpStatus.OK);
    }
}
