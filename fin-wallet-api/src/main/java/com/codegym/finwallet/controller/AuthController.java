package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.AppUserDto;
<<<<<<< HEAD
import com.codegym.finwallet.entity.AppUser;
=======
import com.codegym.finwallet.dto.CommonResponse;
>>>>>>> 3d89660c9940872fd5366d2d03788a89583ddb1c
import com.codegym.finwallet.service.AppUserService;
import com.codegym.finwallet.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
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

<<<<<<< HEAD
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AppUserDto appUserDto) {
//        Authentication authentication =
//    }
=======
    @PostMapping ("/forget-password")
    public ResponseEntity<CommonResponse> forgetPassword(@RequestBody AppUserDto appUserDto) {
        CommonResponse commonResponse = appUserService.forgotPassword(appUserDto);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
>>>>>>> 3d89660c9940872fd5366d2d03788a89583ddb1c
}
