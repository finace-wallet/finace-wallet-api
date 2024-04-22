package com.codegym.finwallet.controller;

import com.codegym.finwallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @PutMapping("/{id}")
    public ResponseEntity<?> create(@PathVariable Long id){
        return new ResponseEntity<>(walletService.edit(id) ,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<?> test(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
