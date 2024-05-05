package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.WalletOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletOwnershipRepository extends JpaRepository<WalletOwnership, Long> {
    List<WalletOwnership> findWalletOwnershipByAppUser(AppUser appUser);

}
