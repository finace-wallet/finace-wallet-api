package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.AuthConstant;
import com.codegym.finwallet.constant.CharacterConstant;
import com.codegym.finwallet.constant.UserConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.ChangePasswordRequest;
import com.codegym.finwallet.dto.payload.request.ForgotPasswordRequest;
import com.codegym.finwallet.dto.payload.request.LoginRequest;
import com.codegym.finwallet.dto.payload.request.RegisterRequest;
import com.codegym.finwallet.dto.payload.response.LoginResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.entity.Role;
import com.codegym.finwallet.entity.TokenBlackList;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.ProfileRepository;
import com.codegym.finwallet.repository.RoleRepository;
import com.codegym.finwallet.repository.TokenBlackListRepository;
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

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final JavaMailSender mailSender;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ProfileRepository profileRepository;
    private final WalletRepository walletRepository;
    private final TokenBlackListRepository tokenBlackListRepository;

    @Override
    public CommonResponse createUser(RegisterRequest request) {
        String email = request.getEmail();
        Optional<AppUser> appUserOptional = appUserRepository.findAppUserByEmail(email);
        if (appUserOptional.isEmpty()  && isRoleExist()) {
            AppUser appUser = new AppUser();
            appUser.setEmail(request.getEmail());
            appUser.setPassword(passwordEncoder.encode(request.getPassword()));
            Role role = roleRepository.findByRoleType(AuthConstant.ROLE_TYPE_USER);
            appUser.setActive(true);
            appUser.setDelete(false);

            Profile profile = new Profile();
            Wallet wallet = new Wallet();
            wallet.setUsers(Collections.singletonList(appUser));
            walletRepository.save(wallet);
            profile.setAppUser(appUser);
            appUserRepository.save(appUser);
            profileRepository.save(profile);

            return CommonResponse.builder()
                    .data(null)
                    .message(UserConstant.CREATE_USER_SUCCESSFUL_MESSAGE)
                    .status(HttpStatus.CREATED)
                    .build();
        }
            return CommonResponse.builder()
                    .data(null)
                    .message(UserConstant.CREATE_USER_FAIL_MESSAGE + email)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

    @Override
    public boolean changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (email != null) {
            AppUser appUser = appUserRepository.findByEmail(email);
            if (passwordEncoder.matches(request.getCurrentPassword(), appUser.getPassword())) {
                appUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
                appUserRepository.save(appUser);
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
            AppUser appUser = appUserRepository.findByEmail(email);
            appUser.setDelete(true);
            appUserRepository.save(appUser);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isRoleExist() {
        return roleRepository.existsByRoleType(AuthConstant.ROLE_TYPE_USER);
    }

    @Override
    public boolean isUserActiveAndNotDelete(String email) {
        AppUser user = appUserRepository.findByEmail(email);
        boolean isUserActive = user.isActive();
        boolean isUserDelete = user.isDelete();
        if (isUserActive && !isUserDelete) {
            return true;
        }
        return false;
    }

    @Override
    public CommonResponse logout(String request) {
        if (request != null && request.startsWith("Bearer ")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String accessToken = request.substring(7);
            String email = authentication.getName();

            if (!isUserTokenInBlackList(email)) {
                AppUser user = appUserRepository.findByEmail(email);
                TokenBlackList tokenBlackList = new TokenBlackList();
                tokenBlackList.setToken(accessToken);
                tokenBlackList.setUser(user);
                tokenBlackListRepository.save(tokenBlackList);
            } else {
                TokenBlackList tokenBlackList = tokenBlackListRepository.findByEmail(email);
                tokenBlackList.setToken(accessToken);
            }

            return CommonResponse.builder()
                    .data(null)
                    .message(UserConstant.LOGOUT_SUCCESSFUL_MESSAGE)
                    .status(HttpStatus.OK)
                    .build();
        }

        return CommonResponse.builder()
                .data(null)
                .message(UserConstant.LOGOUT_FAIL_MESSAGE)
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    @Override
    public boolean isUserTokenInBlackList(String email) {
        TokenBlackList token = tokenBlackListRepository.findByEmail(email);
        return token != null;
    }

    @Override
    public String generatePassword() {
        StringBuilder password = new StringBuilder();

        String allChars = CharacterConstant.CHARACTER_UPPER_CASE
                + CharacterConstant.CHARACTER_LOWER_CASE
                + CharacterConstant.CHARACTER_NUMBERS
                + CharacterConstant.CHARACTER_SPECIALS;

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
    public CommonResponse forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail();
        String newPassword = generatePassword();
        String newPassEncode = passwordEncoder.encode(newPassword);
        return appUserRepository.findAppUserByEmail(email)
                .map(appUser -> {
                    appUser.setPassword(newPassEncode);
                    appUserRepository.save(appUser);
                    sendEmail(email,newPassword);
                    return CommonResponse.builder()
                            .data(null)
                            .message(UserConstant.SEND_PASSWORD_SUCCESSFUL_MESSAGE)
                            .status(HttpStatus.OK)
                            .build();
                })
                .orElse(CommonResponse.builder()
                        .data(null)
                        .message(UserConstant.SEND_PASSWORD_FAIL_MESSAGE+ email)
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @Override
    public CommonResponse login(LoginRequest loginRequest, HttpServletResponse response) {
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
                    .message(UserConstant.MESSAGE_LOGIN_FAIL_AUTHORIZATION + loginRequest.getEmail())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }


        if (authentication.isAuthenticated() && isUserActiveAndNotDelete(authentication.getName())) {
            String accessToken = jwtService.GenerateToken(loginRequest.getEmail());
            LoginResponse loginResponse = modelMapper.map(appUser, LoginResponse.class);
            loginResponse.setAccessToken(accessToken);
            return CommonResponse.builder()
                    .data(loginResponse)
                    .message(UserConstant.MESSAGE_LOGIN_SUCCESS)
                    .status(HttpStatus.OK)
                    .build();
        }
        return CommonResponse.builder()
                .data(null)
                .message(UserConstant.MESSAGE_LOGIN_FAIL)
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

}
