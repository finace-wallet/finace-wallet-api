package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Page<Wallet> findAllByUser(Pageable pageable, AppUser appUser);
    Iterable<Wallet> findAllByUser(AppUser appUser);
}
