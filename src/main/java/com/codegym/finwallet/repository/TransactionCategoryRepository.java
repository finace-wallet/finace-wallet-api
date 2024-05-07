package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
    @Query("SELECT c FROM TransactionCategory c JOIN c.appUser u WHERE u.email = :appUserEmail")
    List<TransactionCategory> findAllByEmail(String appUserEmail);
}