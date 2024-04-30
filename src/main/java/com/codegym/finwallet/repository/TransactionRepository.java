package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.Transaction;
import com.codegym.finwallet.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT wt FROM WalletTransaction wt\n" +
            "WHERE wt.wallet.id = :walletId\n" +
            "AND wt.isDelete = false")
    Page<Transaction> findAllByWalletId(Pageable pageable, Long walletId);
}
