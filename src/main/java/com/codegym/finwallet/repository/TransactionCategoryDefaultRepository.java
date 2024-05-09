package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.TransactionCategoryDefault;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionCategoryDefaultRepository extends JpaRepository<TransactionCategoryDefault, Long> {
    boolean existsByName(String name);
}
