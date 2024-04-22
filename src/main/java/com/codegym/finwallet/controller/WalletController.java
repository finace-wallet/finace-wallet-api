package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.service.impl.WalletServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletServiceImpl walletService;

    @GetMapping
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
    public ResponseEntity<Wallet> createWallet(@RequestBody WalletRequest request) {
        return new ResponseEntity<>(walletService.save(request), HttpStatus.CREATED);
    }

}
