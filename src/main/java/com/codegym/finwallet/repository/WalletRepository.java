package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet,Long> {

    @Query("SELECT w FROM Wallet w JOIN w.walletOwnerships wo JOIN wo.appUser u WHERE u.email = :appUserEmail")
    Page<Wallet> findAllByEmail(Pageable pageable, String appUserEmail);

    @Query("SELECT wo.wallet \n" +
            "FROM WalletOwnership wo \n" +
            "JOIN wo.appUser au\n" +
            "WHERE au.email = :userEmail AND wo.ownerShip.name = 'OWNER'")
    Page<Wallet> findAllByEmailAndOwner(Pageable pageable, String userEmail);

    Optional<Wallet> findById(Long id);

    @Query("SELECT w FROM Wallet w JOIN w.walletOwnerships wo JOIN wo.appUser u WHERE u.email = :appUserEmail")
    List<Wallet>findWalletByEmail(String appUserEmail);



}
