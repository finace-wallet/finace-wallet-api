package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.Spending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpendingRepository extends JpaRepository<Spending, Long> {
}
