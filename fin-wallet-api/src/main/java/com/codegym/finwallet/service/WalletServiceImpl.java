package com.codegym.finwallet.service;

import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Page<Wallet> findAll(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        return walletRepository.findById(id);
    }

    @Override
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public void remove(Long id) {
        walletRepository.deleteById(id);
    }

    @Override
    public Page<Wallet> findAllByUser(Pageable pageable, AppUser appUser) {
        return walletRepository.findAllByUser(pageable,appUser);
    }

    @Override
    public Iterable<Wallet> findAllByUser(AppUser appUser) {
        return walletRepository.findAllByUser(appUser);
    }
}
