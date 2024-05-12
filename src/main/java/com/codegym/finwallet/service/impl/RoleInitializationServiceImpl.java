//package com.codegym.finwallet.service.impl;
//
//import com.codegym.finwallet.entity.Role;
//import com.codegym.finwallet.repository.RoleRepository;
//import com.codegym.finwallet.service.RoleInitializationService;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import static com.codegym.finwallet.constant.AuthConstant.ROLE_TYPE_ADMIN;
//import static com.codegym.finwallet.constant.AuthConstant.ROLE_TYPE_USER;
//
//@Service
//@RequiredArgsConstructor
//public class RoleInitializationServiceImpl implements RoleInitializationService {
//
//    private final RoleRepository roleRepository;
//    @PostConstruct
//    public void init() {
//        initializeRoles();
//    }
//
//    private void initializeRoles() {
//        if(!roleRepository.existsByRoleType(ROLE_TYPE_USER)) {
//            Role userRole = new Role();
//            userRole.setRoleType(ROLE_TYPE_USER);
//            roleRepository.save(userRole);
//        }
//
//        if(!roleRepository.existsByRoleType(ROLE_TYPE_ADMIN)) {
//            Role adminRole = new Role();
//            adminRole.setRoleType(ROLE_TYPE_ADMIN);
//            roleRepository.save(adminRole);
//        }
//    }
//}
