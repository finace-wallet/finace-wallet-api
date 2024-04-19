package com.codegym.finwallet.service.impl;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepo;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    @Override
    public Page<Wallet> findAllByEmail(Pageable pageable, String email) {
        return walletRepository.findAllByEmail(pageable,email);
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
    public Wallet findById(Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        Wallet newWallet = new Wallet();
        if (wallet.isPresent()){
             newWallet = wallet.get();
        }
        return newWallet;
    }

}
