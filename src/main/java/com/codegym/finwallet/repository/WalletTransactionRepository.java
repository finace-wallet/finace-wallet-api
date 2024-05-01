package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long>  {
    WalletTransaction findByTransactionId(Long transactionId);

}
