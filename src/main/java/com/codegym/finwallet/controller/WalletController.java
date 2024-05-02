package com.codegym.finwallet.controller;


import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepository;

import com.codegym.finwallet.service.TransactionService;

import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final TransactionService transactionService;

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> update(@RequestBody WalletRequest walletRequest, @PathVariable Long id){
        CommonResponse commonResponse = walletService.editWallet(walletRequest,id);

        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<CommonResponse> delete(@PathVariable Long id) {
        CommonResponse commonResponse = walletService.deleteWallet(id);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Wallet>> getAllWallet(Pageable pageable){
        Page<Wallet> walletsPage = walletService.findAllByEmail(pageable);
        return new ResponseEntity<>(walletsPage, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Wallet> findById(@PathVariable Long id) {
        Wallet wallet = walletService.findById(id);
        return new ResponseEntity<>(wallet,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createWallet(@RequestBody WalletRequest request) {
        CommonResponse commonResponse = walletService.createWallet(request);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @PostMapping("/transfer")
    public ResponseEntity<CommonResponse> transferMoney(@RequestBody TransferMoneyRequest transferRequest) {
        CommonResponse response = transactionService.transferMoney(transferRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/add-money")
    public ResponseEntity<CommonResponse> addMoneyToWallet(@RequestParam Long walletId,
                                                           @RequestParam double amount) {
        CommonResponse response = walletService.addMoneyToWallet(walletId, amount);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("{id}/transactions")
    public ResponseEntity<CommonResponse> getTransactionHistory(@PathVariable Long id,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        CommonResponse commonResponse = transactionService.findAllTransactionsByWalletId(pageable,id);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
