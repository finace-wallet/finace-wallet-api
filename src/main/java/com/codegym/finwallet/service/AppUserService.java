package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.AppUserDto;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.ChangePasswordRequest;
import com.codegym.finwallet.dto.payload.request.ForgotPasswordRequest;
import com.codegym.finwallet.dto.payload.request.LoginRequest;
import com.codegym.finwallet.dto.payload.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AppUserService {
    CommonResponse createUser(RegisterRequest request);
    String generatePassword();
    void sendEmail(String email, String newPassword);
    CommonResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    CommonResponse login(LoginRequest loginRequest, HttpServletResponse response);
    boolean changePassword(ChangePasswordRequest changePasswordRequest);
    boolean deleteUser();
    boolean isRoleExist();
    boolean isUserActiveAndNotDelete(String email );
    CommonResponse logout(String request);
    boolean isUserTokenInBlackList(String email);


}
