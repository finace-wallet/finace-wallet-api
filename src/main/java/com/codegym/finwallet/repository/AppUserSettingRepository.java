package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.AppUserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserSettingRepository extends JpaRepository<AppUserSetting, Long> {
    Optional<AppUserSetting> findByAppUser_Email(String email);
}
