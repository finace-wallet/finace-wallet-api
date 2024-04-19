package com.codegym.finwallet.controller;

import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepo;
import com.codegym.finwallet.service.JwtService;
import com.codegym.finwallet.service.impl.WalletServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletServiceImpl walletService;
    private final AppUserRepo appUserRepo;
    private final JwtService jwtService;



    @GetMapping
    public ResponseEntity<Page<Wallet>> getAllWallet(Pageable pageable, @RequestHeader("Authorization") String tokenHeader){
        String username = jwtService.extractUsername(tokenHeader);
        Page<Wallet> walletsPage = walletService.findAllByUser(pageable,username);//trả về page(walletDTo)
        return new ResponseEntity<>(walletsPage, HttpStatus.OK);
    }

    @GetMapping("/listWallet")
    public ResponseEntity<Iterable<Wallet>> getAllWallet1(@RequestHeader("Authorization") String tokenHeader) {
        String username = jwtService.extractUsername(tokenHeader);
        Iterable<Wallet> walletsPage = walletService.findAllByUser(username);
        return new ResponseEntity<>(walletsPage,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> findById(@PathVariable Long id) {
        Optional<Wallet> wallet = walletService.findById(id);
        return new ResponseEntity<>(wallet.get(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Wallet> save(@RequestBody Wallet wallet) {
        return new ResponseEntity<>(walletService.save(wallet), HttpStatus.CREATED);
    }

//    @PostMapping("/upload")
//    public ResponseEntity<Wallet> createWallet(@ModelAttribute Wallet wallet,@RequestHeader("Authorization") String tokenHeader){
//        String username = jwtService.extractUsername(tokenHeader);
//        Wallet wallets = new Wallet();
//        wallets.setAmount(wallet.getAmount());
//        wallets.setIcon(wallet.getIcon());
//        wallets.setName(wallet.getName());
//        wallets.setDescription(wallet.getDescription());
//        wallets.setCurrentType(wallet.getCurrentType());
//        wallets.setUser(appUserRepo.findByUsername(username));
//        walletService.save(wallet);
//
//    }
}
