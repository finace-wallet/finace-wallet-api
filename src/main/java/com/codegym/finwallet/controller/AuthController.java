package com.codegym.finwallet.controller;

import com.codegym.finwallet.constant.UserConstant;
import com.codegym.finwallet.dto.AppUserDto;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.ChangePasswordRequest;
import com.codegym.finwallet.dto.payload.request.LoginRequest;
import com.codegym.finwallet.service.AppUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AppUserService appUserService;

    @Autowired
    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUserDto appUserDto) {
        appUserService.saveUser(appUserDto);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        boolean isChanged = appUserService.changePassword(request);
        if (isChanged) {
            return ResponseEntity.ok().body(UserConstant.CHANGE_PASSWORD_SUCCESSFUL);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserConstant.USER_NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    private ResponseEntity<?> deleteUserById() {
        boolean delete = appUserService.deleteUser();
        if (delete) {
            return ResponseEntity.ok().body(UserConstant.DELETE_USER_SUCCESSFUL);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserConstant.USER_NOT_FOUND);
        }
    }


    @PostMapping ("/forget-password")
    public ResponseEntity<CommonResponse> forgetPassword(@RequestBody AppUserDto appUserDto) {
        CommonResponse commonResponse = appUserService.forgotPassword(appUserDto);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        CommonResponse commonResponse = appUserService.Login(loginRequest,response);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
