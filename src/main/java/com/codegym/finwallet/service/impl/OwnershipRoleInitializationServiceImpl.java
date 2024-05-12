//package com.codegym.finwallet.service.impl;
//
//import com.codegym.finwallet.entity.OwnerShip;
//import com.codegym.finwallet.repository.OwnerShipRepository;
//import com.codegym.finwallet.service.OwnershipRoleInitializationService;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import static com.codegym.finwallet.constant.WalletOwnershipConstant.OWNERSHIP_CO_OWNER;
//import static com.codegym.finwallet.constant.WalletOwnershipConstant.OWNERSHIP_OWNER;
//import static com.codegym.finwallet.constant.WalletOwnershipConstant.OWNERSHIP_VIEWER;
//
//@Service
//@RequiredArgsConstructor
//public class OwnershipRoleInitializationServiceImpl implements OwnershipRoleInitializationService {
//    private final OwnerShipRepository ownerShipRepository;
//    @PostConstruct
//    public void init() {
//        initializeOwnership();
//    }
//
//    private void initializeOwnership() {
//        if(!ownerShipRepository.existsByName(OWNERSHIP_OWNER)) {
//            OwnerShip ownership = new OwnerShip();
//            ownership.setName(OWNERSHIP_OWNER);
//            ownerShipRepository.save(ownership);
//        }
//
//        if(!ownerShipRepository.existsByName(OWNERSHIP_CO_OWNER)) {
//            OwnerShip ownership = new OwnerShip();
//            ownership.setName(OWNERSHIP_CO_OWNER);
//            ownerShipRepository.save(ownership);
//        }
//
//        if(!ownerShipRepository.existsByName(OWNERSHIP_VIEWER)) {
//            OwnerShip ownership = new OwnerShip();
//            ownership.setName(OWNERSHIP_VIEWER);
//            ownerShipRepository.save(ownership);
//        }
//
//    }
//}
