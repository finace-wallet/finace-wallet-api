package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.VarConstant;
import com.codegym.finwallet.dto.AppUserDto;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Role;
import com.codegym.finwallet.repository.AppUserRepo;
import com.codegym.finwallet.repository.RoleRepo;
import com.codegym.finwallet.service.AppUserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepo appUserRepo;
    private PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;

    public AppUserServiceImpl(AppUserRepo appUserRepo, PasswordEncoder passwordEncoder, RoleRepo roleRepo, ModelMapper modelMapper) {
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

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
}
