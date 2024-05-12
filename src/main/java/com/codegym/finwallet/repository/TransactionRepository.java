package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.transactionCategory.id = :transactionCategoryId AND t.isDelete = false")
    Page<Transaction> findAllByWalletIdAndTransactionCategoryId(Long walletId, Long transactionCategoryId, Pageable pageable);
    @Query("SELECT t FROM Transaction t WHERE t.wallet.id = :walletId AND t.isDelete = false")
    Page<Transaction> findAllByWalletId(Long walletId, Pageable pageable);
    @Query("SELECT COUNT(t), SUM(t.amount) " +
            "FROM Transaction t " +
            "WHERE (:categoryId IS NULL " +
            "OR t.transactionCategory.id = :categoryId) " +
            "AND t.wallet.id = :walletId " +
            "AND t.isDelete = false")
    List<Object[]> getTotalTransactionAndAmountByTransactionCategory(Long categoryId, Long walletId);

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.transactionDate >= :startDate AND t.transactionDate <= :endDate AND t.wallet.id = :walletId " +
            "AND t.isDelete = false")
    List<Transaction> findTransactionForTime(LocalDate startDate, LocalDate endDate, Long walletId);
}
