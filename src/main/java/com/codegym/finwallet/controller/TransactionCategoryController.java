package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.service.TransactionCategoryDefaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction-categories")
public class TransactionCategoryController {
    private final TransactionCategoryDefaultService categoryDefaultService;
    @GetMapping("/list-category-default")
    public ResponseEntity<CommonResponse> listCategoryDefault() {
        CommonResponse commonResponse = categoryDefaultService.findAllDefaultCategory();
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
