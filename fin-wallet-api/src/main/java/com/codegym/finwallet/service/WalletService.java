package com.codegym.finwallet.service;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WalletService extends GeneralService<Wallet> {
    Page<Wallet> findAllByUser(Pageable pageable, AppUser appUser);
    Iterable<Wallet> findAllByUser(AppUser appUser);
    Wallet edit(Long id);
}
