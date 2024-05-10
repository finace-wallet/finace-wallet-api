package com.codegym.finwallet.controller;


import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.AddMoneyRequest;
import com.codegym.finwallet.dto.payload.request.DisplayRecipientRequest;
import com.codegym.finwallet.dto.payload.request.TransactionCategoryRequest;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.TransactionCategoryService;
import com.codegym.finwallet.service.TransactionService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final TransactionCategoryService transactionCategoryService;
    private final WalletRepository walletRepository;
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
    public ResponseEntity<CommonResponse> getAllWallet(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page,size);
        CommonResponse commonResponse = walletService.findAllByEmail(pageable);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @GetMapping("/list/owner")
    public ResponseEntity<CommonResponse> getAllWalletOwner(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page,size);
        CommonResponse commonResponse = walletService.findWalletsByEmailAndOwner(pageable);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @GetMapping("/list/viewer")
    public ResponseEntity<CommonResponse> getAllWalletViewer(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page,size);
        CommonResponse commonResponse = walletService.findWalletByViewer(pageable);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @GetMapping("/list/co-owner")
    public ResponseEntity<CommonResponse> getAllWalletCoOwner(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page,size);
        CommonResponse commonResponse = walletService.findWalletsByCoOwner(pageable);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @PostMapping("/display-recipient")
    public ResponseEntity<Page<Wallet>> getAllRecipientWallet(@RequestBody DisplayRecipientRequest request){
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Wallet> walletsPage = walletService.findAllRecipientByEmail(pageable, request.getTransferEmail());
        return new ResponseEntity<>(walletsPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> findById(@PathVariable Long id) {
        CommonResponse commonResponse = walletService.findById(id);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createWallet(@RequestBody WalletRequest request) {
        CommonResponse commonResponse = walletService.createWallet(request);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }


    @PostMapping("/add-money")
    public ResponseEntity<CommonResponse> addMoneyToWallet(@RequestBody AddMoneyRequest request) {
        CommonResponse response = walletService.addMoneyToWallet(request.getWalletId(), request.getAmount());
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getWalletDetailsById(@PathVariable Long id) {
        Optional<Wallet> walletOptional = walletRepository.findById(id);
        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            return ResponseEntity.ok(wallet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/create-transaction-category")
    public ResponseEntity<CommonResponse> createTransactionCategory(@PathVariable Long id, @RequestBody TransactionCategoryRequest request) {
        CommonResponse commonResponse = transactionCategoryService.createTransactionCategory(request,id);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @PostMapping("/{id}/create-transaction")
    public ResponseEntity<CommonResponse> createTransaction(@PathVariable Long id, @RequestBody TransactionRequest request) {
        CommonResponse commonResponse = transactionService.saveTransaction(request,id);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @GetMapping("/{id}/list-transaction")
    public ResponseEntity<CommonResponse> getTransactionList(@PathVariable Long id ,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page,size);
        CommonResponse commonResponse = transactionService.findAllTransactionsByWalletId(id,pageable);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @GetMapping("/{id}/list-transaction/category")
    public ResponseEntity<CommonResponse> getTransactionListByCategory(@PathVariable Long id,
                                                                       @RequestParam Long categoryId,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "5") int size
                                                                       ){
        Pageable pageable = PageRequest.of(page,size);
        CommonResponse commonResponse = transactionService.findAllTransactionsByCategory(id,categoryId,pageable);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}


