package com.codegym.finwallet.repository;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
    @Query("SELECT w FROM Wallet w JOIN w.user u WHERE u.email = :appUserEmail")
    Page<Wallet> findAllByEmail(Pageable pageable, String appUserEmail);

    Optional<Wallet> findById(Long id);
}