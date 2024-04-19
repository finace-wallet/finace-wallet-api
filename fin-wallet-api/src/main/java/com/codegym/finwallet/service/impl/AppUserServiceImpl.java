package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.VarConstant;
import com.codegym.finwallet.dto.AppUserDto;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Role;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.RoleRepository;
import com.codegym.finwallet.service.AppUserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public AppUserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void saveUser(AppUserDto appUserDto) {
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserDto.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUserDto.getPassword()));

        Role role = roleRepository.findByRoleType(VarConstant.ROLE_TYPE_USER);
        appUser.setRoles(Arrays.asList(role));
        appUser.setActive(true);
        appUserRepository.save(appUser);
    }
}
