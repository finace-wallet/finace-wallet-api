package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.AppUserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppUserSettingRepository extends JpaRepository<AppUserSetting, Long> {
    Optional<AppUserSetting> findByAppUser_Email(String email);

    @Query("SELECT aus.appUser.email FROM AppUserSetting aus WHERE aus.type = 'MONTH'")
    List<String> getListEmailTypeMonth();

    @Query("SELECT aus.appUser.email FROM AppUserSetting aus WHERE aus.type = 'WEEK'")
    List<String> getListEmailTypeWeek();
    @Query("SELECT aus.appUser.email FROM AppUserSetting aus WHERE aus.type = 'DAY'")
    List<String> getListEmailTypeDay();
}
