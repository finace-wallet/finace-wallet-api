package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.UserDefType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDefTypeRepository extends JpaRepository<UserDefType,Long> {
    Optional<UserDefType> findById(Long id);
}
