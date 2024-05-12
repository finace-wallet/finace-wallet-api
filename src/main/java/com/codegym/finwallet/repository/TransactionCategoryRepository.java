package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
    Optional <TransactionCategory> findByName(String name);
    @Query("SELECT tc FROM TransactionCategory tc WHERE tc.wallet.id = :walletId AND tc.type = :categoryType")
    List<TransactionCategory> findAllByWalletIdAndType(Long walletId, String categoryType);
}
