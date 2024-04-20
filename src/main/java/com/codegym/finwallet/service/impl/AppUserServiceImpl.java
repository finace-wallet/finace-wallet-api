package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.AuthConstant;
import com.codegym.finwallet.constant.UserConstant;
import com.codegym.finwallet.dto.AppUserDto;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.ChangePasswordRequest;
import com.codegym.finwallet.dto.payload.request.LoginRequest;
import com.codegym.finwallet.dto.payload.response.LoginResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.entity.Role;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepo;
import com.codegym.finwallet.repository.ProfileRepository;
import com.codegym.finwallet.repository.RoleRepo;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.AppUserService;
import com.codegym.finwallet.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;
    private final JavaMailSender mailSender;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ProfileRepository profileRepository;
    private final WalletRepository walletRepository;

    @Override
    public void saveUser(AppUserDto appUserDto) {
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserDto.getUsername());
        appUser.setEmail(appUserDto.getEmail());
        appUser.setPassword(passwordEncoder.encode(appUserDto.getPassword()));

        Role role = roleRepo.findByRoleType(AuthConstant.ROLE_TYPE_USER);
        appUser.setRoles(Collections.singletonList(role));
        appUser.setActive(true);
        Profile profile = new Profile();
        Wallet wallet = new Wallet();
        walletRepository.save(wallet);
        profile.setAppUser(appUser);
        appUserRepo.save(appUser);
        profileRepository.save(profile);
    }

    @Override
    public boolean changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (email != null) {
            AppUser appUser = appUserRepo.findByEmail(email);
            if (passwordEncoder.matches(request.getCurrentPassword(), appUser.getPassword())) {
                appUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
                appUserRepo.save(appUser);
                return true;
            }
        }
        return  false;
    }

    @Override
    public boolean deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();;
        String email = authentication.getName();
        if (email != null) {
            AppUser appUser = appUserRepo.findByEmail(email);
            appUser.setDelete(true);
            appUserRepo.save(appUser);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String generatePassword() {
        StringBuilder password = new StringBuilder();
        String upperCaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numericChars = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[{]}|;:,<.>?";

        String allChars = upperCaseChars + lowerCaseChars + numericChars + specialChars;

        Random random = new Random();

        for (int i = 0; i < UserConstant.PASSWORD_MAX_LENGTH; i++) {
            int randomIndex = random.nextInt(allChars.length());
            password.append(allChars.charAt(randomIndex));
        }

        return password.toString();
    }

    @Override
    public void sendEmail(String email, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(UserConstant.SUBJECT);
        message.setText(newPassword);
        mailSender.send(message);
    }

    @Override
    public CommonResponse forgotPassword(AppUserDto appUserDto) {
        String email = appUserDto.getEmail();
        String newPassword = generatePassword();
        String newPassEncode = passwordEncoder.encode(newPassword);
        return appUserRepo.findAppUserByEmail(email)
                .map(appUser -> {
                    appUser.setPassword(newPassEncode);
                    appUserRepo.save(appUser);
                    sendEmail(email,newPassword);
                    return CommonResponse.builder()
                            .data(null)
                            .message("Gửi mật khẩu mới của người dùng về mail thành công!")
                            .status(HttpStatus.OK)
                            .build();
                })
                .orElse(CommonResponse.builder()
                        .data(null)
                        .message("Email không tồn tại!")
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @Override
    public CommonResponse Login(LoginRequest loginRequest, HttpServletResponse response) {
        AppUser appUser = modelMapper.map(loginRequest, AppUser.class);
        Authentication authentication ;
        try{
           authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            appUser.getEmail(),
                            appUser.getPassword()
                    )
            );
        }catch (AuthenticationException e){
            return CommonResponse.builder()
                    .data(null)
                    .message(UserConstant.MESSAGE_LOGIN_FAIL + loginRequest.getEmail())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }


        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.GenerateToken(loginRequest.getEmail());
            LoginResponse loginResponse = modelMapper.map(appUser, LoginResponse.class);
            loginResponse.setAccessToken(accessToken);
            return CommonResponse.builder()
                    .data(loginResponse)
                    .message(UserConstant.MESSAGE_LOGIN_SUCCESS)
                    .status(HttpStatus.OK)
                    .build();
        }
        return null;
    }
}