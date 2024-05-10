package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllByWalletIdAndTransactionCategoryId(Long walletId, Long transactionCategoryId, Pageable pageable);
    Page<Transaction> findAllByWalletId(Long walletId, Pageable pageable);
}
