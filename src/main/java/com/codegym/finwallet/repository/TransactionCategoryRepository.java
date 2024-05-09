package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
    boolean existsByName(String name);
}
