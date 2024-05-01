package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT u.profile FROM AppUser u WHERE u.email = :email")
    Optional<Profile> findProfileByEmail(String email);
}

