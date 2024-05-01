package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long>  {
    WalletTransaction findByTransactionId(Long transactionId);
}
