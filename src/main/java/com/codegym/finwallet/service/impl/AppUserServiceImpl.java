package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.AuthConstant;
import com.codegym.finwallet.constant.CharacterConstant;
import com.codegym.finwallet.constant.UserConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.ActiveUserRequest;
import com.codegym.finwallet.dto.payload.request.ChangePasswordRequest;
import com.codegym.finwallet.dto.payload.request.ForgotPasswordRequest;
import com.codegym.finwallet.dto.payload.request.LoginRequest;
import com.codegym.finwallet.dto.payload.request.RegisterRequest;
import com.codegym.finwallet.dto.payload.response.LoginResponse;
import com.codegym.finwallet.dto.payload.response.RoleResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.AppUserSetting;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.entity.Role;
import com.codegym.finwallet.entity.TokenBlackList;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.AppUserSettingRepository;
import com.codegym.finwallet.repository.ProfileRepository;
import com.codegym.finwallet.repository.RoleRepository;
import com.codegym.finwallet.repository.TokenBlackListRepository;
import com.codegym.finwallet.service.AppUserService;
import com.codegym.finwallet.service.JwtService;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final Cache<String, String> otpCache;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final JavaMailSender mailSender;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ProfileRepository profileRepository;
    private final TokenBlackListRepository tokenBlackListRepository;
    private final SpringTemplateEngine templateEngine;
    private final AppUserSettingRepository settingRepository;
    private final AppUserSettingRepository appUserSettingRepository;

    @Override
    public CommonResponse createUser(RegisterRequest request) {
        String email = request.getEmail();
        String otp = null;
        Optional<AppUser> appUserOptional = appUserRepository.findAppUserByEmail(email);
        if (appUserOptional.isEmpty() && isRoleExist()) {
            AppUser appUser = new AppUser();
            appUser.setEmail(request.getEmail());
            appUser.setPassword(passwordEncoder.encode(request.getPassword()));
            Role role = roleRepository.findByRoleType(AuthConstant.ROLE_TYPE_USER);
            appUser.setActive(false);
            appUser.setDelete(false);
            appUser.setRoles(Collections.singletonList(role));
            Profile profile = new Profile();
            profile.setAppUser(appUser);
            AppUserSetting appUserSetting = new AppUserSetting();
            appUserSetting.setAppUser(appUser);
            appUserRepository.save(appUser);
            profileRepository.save(profile);
            appUserSettingRepository.save(appUserSetting);
            otp = generateOtp();
            saveOtpAndEmail(otp, email);
            try {
                sendOtpToEmail(otp, email);
                return CommonResponse.builder()
                        .data(null)
                        .message(UserConstant.CREATE_USER_SUCCESSFUL_MESSAGE)
                        .status(HttpStatus.CREATED)
                        .build();
            } catch (MessagingException e) {
                return CommonResponse.builder()
                        .data(null)
                        .message(UserConstant.CREATE_USER_FAIL_MESSAGE + email)
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
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
        return false;
    }

    @Override
    public boolean deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ;
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
    public CommonResponse activeUser(ActiveUserRequest activeUserRequest) {
        String otp = activeUserRequest.getOtp();
        try {
            String email = getEmailByOtp(otp);
            AppUser user = appUserRepository.findByEmail(email);
            user.setActive(true);
            appUserRepository.save(user);
            otpCache.invalidate(otp);

            return CommonResponse.builder()
                    .data(null)
                    .message(UserConstant.APP_USER_ACTIVE_SUCCESSFUL)
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return CommonResponse.builder()
                    .data(null)
                    .message(UserConstant.APP_USER_ACTIVE_FAIL_MESSAGE)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
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
    public void sendNewPasswordToEmail(String email, String newPassword) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject(UserConstant.FORGET_PASSWORD_SUBJECT);

        Context context = new Context();
        context.setVariable("newPassword", newPassword);
        String emailContent = templateEngine.process("new-password-email-template", context);

        helper.setText(emailContent, true);

        mailSender.send(mimeMessage);

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
                    try {
                        sendNewPasswordToEmail(email, newPassword);
                        return CommonResponse.builder()
                                .data(null)
                                .message(UserConstant.SEND_PASSWORD_SUCCESSFUL_MESSAGE)
                                .status(HttpStatus.OK)
                                .build();
                    } catch (MessagingException e) {
                        System.out.println(e.getMessage());
                        return CommonResponse.builder()
                                .data(null)
                                .message(UserConstant.SEND_PASSWORD_FAIL_MESSAGE + email)
                                .status(HttpStatus.OK)
                                .build();
                    }
                })
                .orElse(CommonResponse.builder()
                        .data(null)
                        .message(UserConstant.SEND_PASSWORD_FAIL_MESSAGE + email)
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @Override
    public CommonResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        AppUser appUser = modelMapper.map(loginRequest, AppUser.class);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            appUser.getEmail(),
                            appUser.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            return CommonResponse.builder()
                    .data(null)
                    .message(UserConstant.MESSAGE_LOGIN_FAIL_AUTHORIZATION + loginRequest.getEmail())
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }


        if (authentication.isAuthenticated() && isUserActiveAndNotDelete(authentication.getName())) {
            List<Role> roles = getUserRole(authentication.getName());
            List<RoleResponse> rolesResponse = mapRoles(roles);
            String fullName = getFullName(authentication.getName());
            String accessToken = jwtService.GenerateToken(loginRequest.getEmail());
            LoginResponse loginResponse = modelMapper.map(appUser, LoginResponse.class);
            loginResponse.setAccessToken(accessToken);
            loginResponse.setRoles(rolesResponse);
            loginResponse.setFullName(fullName);
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

    private String generateOtp() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < UserConstant.OTP_MAX_LENGTH; i++) {
            otp.append(random.nextInt(UserConstant.OTP_MAX_VALUE));
        }
        return otp.toString();
    }

    private void saveOtpAndEmail(String otp, String email) {
        otpCache.put(otp, email);
    }

    private String getEmailByOtp(String otp) {
        return otpCache.getIfPresent(otp);
    }

    private void sendOtpToEmail(String otp, String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject(UserConstant.ACTIVE_USER_SUBJECT);

        Context context = new Context();
        context.setVariable("otp", otp);
        String emailContent = templateEngine.process("otp-active-user-email-template", context);

        helper.setText(emailContent, true);

        mailSender.send(mimeMessage);
    }

    private List<Role> getUserRole(String email) {
        return roleRepository.findRolesByEmail(email);
    }

    private List<RoleResponse> mapRoles(List<Role> roles) {
        return roles.stream()
                .map(this::mapRoleToRoleResponse)
                .collect(Collectors.toList());
    }

    private RoleResponse mapRoleToRoleResponse(Role role) {
        return modelMapper.map(role, RoleResponse.class);
    }

    private String getFullName(String email) {
        Optional<Profile> profileOptional = profileRepository.findProfileByEmail(email);
        return profileOptional.map(Profile::getFullName).orElse(null);
    }
}
