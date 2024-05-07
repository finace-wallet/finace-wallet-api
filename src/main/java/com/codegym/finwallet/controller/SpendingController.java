package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.SpendingRequest;
import com.codegym.finwallet.service.SpendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/spending")
@RequiredArgsConstructor
public class SpendingController {
    private final SpendingService spendingService;
    @PostMapping
    public ResponseEntity<CommonResponse> addSpending(@RequestBody SpendingRequest spendingRequest){
        CommonResponse commonResponse = spendingService.addSpending(spendingRequest);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
