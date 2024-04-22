package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    AppUser findFirstByUsername(String username);
    Optional<AppUser> findAppUserByEmail(String email);
    AppUser findByEmail(String email);
}
