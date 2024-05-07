package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.CommonResponse;

import com.codegym.finwallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/history/")
    public ResponseEntity<CommonResponse> getTransactionHistory(@RequestParam Long walletId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size
                                                                ) {
        PageRequest pageable = PageRequest.of(page, size);
        CommonResponse commonResponse = transactionService.findAllTransactionsByWalletId(pageable,walletId);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
