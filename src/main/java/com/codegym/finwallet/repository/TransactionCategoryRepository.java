package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
    Optional <TransactionCategory> findByName(String name);
}
