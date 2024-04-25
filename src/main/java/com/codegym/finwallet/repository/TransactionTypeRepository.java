package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionTypeRepository extends JpaRepository<TransactionType,Long> {
    Optional<TransactionType> findById(Long id);
}
