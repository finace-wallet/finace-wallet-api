package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.AppUserDto;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.entity.AppUser;

public interface AppUserService {
    void saveUser(AppUserDto appUserDto);

    String generatePassword();

    void sendEmail(String email, String newPassword);

    CommonResponse forgotPassword(AppUserDto appUserDto);
}
