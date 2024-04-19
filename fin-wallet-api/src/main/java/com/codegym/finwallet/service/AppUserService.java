package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.AppUserDto;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.LoginRequest;
import com.codegym.finwallet.dto.payload.request.UpdateProfileRequest;
import com.codegym.finwallet.entity.AppUser;
import jakarta.servlet.http.HttpServletResponse;

public interface AppUserService {
    void saveUser(AppUserDto appUserDto);

    String generatePassword();

    void sendEmail(String email, String newPassword);

    CommonResponse forgotPassword(AppUserDto appUserDto);

    CommonResponse Login(LoginRequest loginRequest, HttpServletResponse response);

}
