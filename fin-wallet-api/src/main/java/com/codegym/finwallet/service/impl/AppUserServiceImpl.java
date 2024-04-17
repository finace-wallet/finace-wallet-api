package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.VarConstant;
import com.codegym.finwallet.dto.AppUserDto;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Role;
import com.codegym.finwallet.repository.AppUserRepo;
import com.codegym.finwallet.repository.RoleRepo;
import com.codegym.finwallet.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;
    private int passwordLength = VarConstant.PASSWORD_MAX_LENGTH;
    private String SUBJECT = VarConstant.SUBJECT;
    private final JavaMailSender mailSender;


    @Override
    public void saveUser(AppUserDto appUserDto) {
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserDto.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUserDto.getPassword()));

        Role role = roleRepo.findByRoleType(VarConstant.ROLE_TYPE_USER);
        appUser.setRoles(Arrays.asList(role));
        appUser.setActive(true);
        appUserRepo.save(appUser);
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

        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = random.nextInt(allChars.length());
            password.append(allChars.charAt(randomIndex));
        }

        return password.toString();
    }

    @Override
    public void sendEmail(String email, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(SUBJECT);
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


}
