package com.codegym.finwallet.repository;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet,Long> {

//    @Query("SELECT w FROM Wallet w JOIN w.users u WHERE u.email = :appUserEmail")
    @Query("SELECT w FROM Wallet w JOIN w.walletOwnerships wo JOIN wo.appUser u WHERE u.email = :appUserEmail")
    Page<Wallet> findAllByEmail(Pageable pageable, String appUserEmail);



//    @Query("SELECT w FROM Wallet w JOIN w.walletOwnerships wo JOIN wo.appUser u WHERE u.email = :email AND wo.ownership = :ownership")
//    Page<Wallet> findAllByEmailAndOwner(Pageable pageable, String email, String ownership);

    @Query("SELECT w FROM Wallet w JOIN w.walletOwnerships wo JOIN wo.appUser u WHERE u.email = :email AND wo.ownership = :ownership AND wo.appUser.id = :currentUserId")
    Page<Wallet> findAllByEmailAndOwner(Pageable pageable, String email, String ownership, Long currentUserId);

    Optional<Wallet> findById(Long id);
//    @Query("SELECT w FROM Wallet w JOIN w.users u WHERE u.email = :appUserEmail")
    @Query("SELECT w FROM Wallet w JOIN w.walletOwnerships wo JOIN wo.appUser u WHERE u.email = :appUserEmail")
    List<Wallet>findWalletByEmail(String appUserEmail);



}
