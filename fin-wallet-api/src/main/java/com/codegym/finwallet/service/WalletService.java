package com.codegym.finwallet.service;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WalletService {
    Page<Wallet> findAllByUser(Pageable pageable, String username);
    Iterable<Wallet> findAllByUser(String username);
    Wallet save(Wallet wallet);
    void remove(Long id);
    Page<Wallet> findAll(Pageable pageable);
    Optional<Wallet> findById(Long id);
}
