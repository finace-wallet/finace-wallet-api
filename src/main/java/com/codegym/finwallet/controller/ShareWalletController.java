package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.ShareWalletRequest;
import com.codegym.finwallet.service.WalletShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class ShareWalletController {
    private final WalletShareService walletShareService;
    @PostMapping("/share")
    public ResponseEntity<CommonResponse> shareWallet(@RequestBody ShareWalletRequest request) {
        CommonResponse commonResponse = walletShareService.walletShare(request.getShareEmail(), request.getAccessLevel(), request.getShareWalletId());
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
