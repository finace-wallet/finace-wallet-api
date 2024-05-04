package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.TransactionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Long> {
    @Query("SELECT c FROM TransactionCategory c JOIN c.appUser u WHERE u.email = :appUserEmail")
    Page<TransactionCategory> findAllByEmail(Pageable pageable, String appUserEmail);
}
