package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.service.TransactionCategoryDefaultService;
import com.codegym.finwallet.service.TransactionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction-categories")
public class TransactionCategoryController {
    private final TransactionCategoryDefaultService categoryDefaultService;
    private final TransactionCategoryService transactionCategoryService;

    @GetMapping("/list-category-default")
    public ResponseEntity<CommonResponse> listCategoryDefault() {
        CommonResponse commonResponse = categoryDefaultService.findAllDefaultCategory();
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @PostMapping("/{walletId}/{categoryId}/add-budget")
    public ResponseEntity<CommonResponse> createBudgetForTransactionCategory(@RequestParam double budget, @PathVariable Long walletId, @PathVariable Long categoryId) {
        CommonResponse commonResponse = transactionCategoryService.createBudget( walletId, categoryId, budget);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
