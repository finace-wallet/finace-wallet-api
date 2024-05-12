package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet,Long> {

    @Query("SELECT w FROM Wallet w JOIN w.walletOwnerships wo JOIN wo.appUser u WHERE u.email = :appUserEmail AND w.isDelete = false")
    Page<Wallet> findAllByEmail(Pageable pageable, String appUserEmail);

    @Query("SELECT wo.wallet \n" +
            "FROM WalletOwnership wo \n" +
            "JOIN wo.appUser au\n" +
            "WHERE au.email = :userEmail AND wo.ownerShip.name = 'OWNER'")
    Page<Wallet> findAllByEmailAndOwner(Pageable pageable, String userEmail);

    @Query("SELECT wo.wallet \n" +
            "FROM WalletOwnership wo \n" +
            "JOIN wo.appUser au\n" +
            "WHERE au.email = :userEmail AND wo.ownerShip.name = 'VIEWER'")
    Page<Wallet> findAllByEmailAndViewer(Pageable pageable, String userEmail);

    @Query("SELECT wo.wallet \n" +
            "FROM WalletOwnership wo \n" +
            "JOIN wo.appUser au\n" +
            "WHERE au.email = :userEmail AND wo.ownerShip.name = 'CO-OWNER'")
    Page<Wallet> findAllByCoOwner(Pageable pageable, String userEmail);

    Optional<Wallet> findById(Long id);

    @Query("SELECT w FROM Wallet w JOIN w.walletOwnerships wo JOIN wo.appUser u WHERE u.email = :appUserEmail AND w.isDelete = false ")
    List<Wallet>findWalletByEmail(String appUserEmail);

    @Query("SELECT SUM(w.amount) FROM Wallet w JOIN w.walletOwnerships wo JOIN wo.appUser u WHERE u.email = :email AND w.isDelete = false")
    Double getWalletAmountByEmail(String email);
}
