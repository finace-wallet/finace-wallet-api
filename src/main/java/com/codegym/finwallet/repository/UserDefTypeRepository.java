package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.UserDefType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDefTypeRepository extends JpaRepository<UserDefType, Long> {
}
