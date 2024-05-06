package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.WalletOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletOwnershipRepository extends JpaRepository<WalletOwnership, Long> {
    List<WalletOwnership> findWalletOwnershipByAppUser(AppUser appUser);

    Optional<WalletOwnership> findByAppUserEmailAndIsDeleteFalse(String email);

    Optional<WalletOwnership> findByAppUserEmailAndWalletIdAndIsDeleteFalse(String email, Long id);
}
