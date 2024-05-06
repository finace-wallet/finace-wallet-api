package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionCategoryRequest;
import com.codegym.finwallet.entity.TransactionCategory;
import com.codegym.finwallet.service.TransactionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transactionCategories")
@RequiredArgsConstructor
public class TransactionCategoryController {
    private final TransactionCategoryService transactionCategoryService;
    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createTransactionCategory(@RequestBody TransactionCategoryRequest request) {
        CommonResponse commonResponse = transactionCategoryService.createTransactionCategory(request);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<List<TransactionCategory>> getAllTransactionCategory() {
        List<TransactionCategory> categories = transactionCategoryService.findAllTransactionCategoryByEmail();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
