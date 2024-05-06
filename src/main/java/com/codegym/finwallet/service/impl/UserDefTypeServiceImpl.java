package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.entity.UserDefType;
import com.codegym.finwallet.repository.UserDefTypeRepository;
import com.codegym.finwallet.service.UserDefTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDefTypeServiceImpl implements UserDefTypeService {
    private final UserDefTypeRepository userDefTypeRepository;


    @Override
    public CommonResponse updateWalletLimit(Long id, double newLimit) {
        if (newLimit < 0) {
            return CommonResponse.builder()
                    .data(null)
                    .message("New wallet limit cannot be less than 0.")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Optional<UserDefType> userDefTypeOptional = userDefTypeRepository.findById(id);
        if (userDefTypeOptional.isEmpty()) {
            return CommonResponse.builder()
                    .data(null)
                    .message("Invalid user definition type Id: " + id)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        UserDefType userDefType = userDefTypeOptional.get();
        userDefType.setWalletLimit(newLimit);
        userDefTypeRepository.save(userDefType);
        return CommonResponse.builder()
                .data(userDefType)
                .message("Update successful!")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public CommonResponse addWalletLimit(Long id, double additionalLimit) {
        if (additionalLimit < 0) {
            return CommonResponse.builder()
                    .data(null)
                    .message("Additional limit cannot be less than 0.")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Optional<UserDefType> userDefTypeOptional = userDefTypeRepository.findById(id);
        if (userDefTypeOptional.isEmpty()) {
            return CommonResponse.builder()
                    .data(null)
                    .message("Invalid user definition type Id: " + id)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        UserDefType userDefType = userDefTypeOptional.get();
        double limit = userDefType.getWalletLimit();
        double newLimit = limit + additionalLimit;
        userDefType.setWalletLimit(newLimit);
        userDefTypeRepository.save(userDefType);
        return CommonResponse.builder()
                .data(userDefType)
                .message("Additional wallet limit added successfully.")
                .build();
    }
}