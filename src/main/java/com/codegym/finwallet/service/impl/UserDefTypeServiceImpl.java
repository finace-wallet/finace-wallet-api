package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.UserConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.UserDefTypeRequest;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.entity.UserDefType;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.UserDefTypeRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.UserDefTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserDefTypeServiceImpl implements UserDefTypeService {
    private final UserDefTypeRepository userDefTypeRepository;
    private final ModelMapper modelMapper;
    private final WalletRepository walletRepository;

    @Override
    public CommonResponse createUserDefType(UserDefTypeRequest request) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Optional<Wallet> walletOptional = walletRepository.findById(request.getWalletId());
            if(walletOptional.isPresent()) {
                Wallet wallet = walletOptional.get();

                UserDefType userDefType = UserDefType.builder()
                        .name(request.getName())
                        .walletLimit(request.getWalletLimit())
                        .isDelete(false)
                        .wallet(wallet)
                        .build();

                userDefTypeRepository.save(userDefType);

                commonResponse = CommonResponse.builder()
                        .data(null)
                        .message(UserConstant.CREATE_USER_DEF_TYPE_SUCCESSFUL)
                        .status(HttpStatus.OK)
                        .build();
            } else {
                commonResponse = CommonResponse.builder()
                        .data(null)
                        .message("Wallet not found for id:" + request.getWalletId())
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

        }catch (SecurityException e){
            commonResponse = commonResponse.builder()
                    .data(null)
                    .message(UserConstant.CREATE_USER_DEF_TYPE_FAIL)
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return commonResponse;
    }

    @Override
    public CommonResponse deleteUserDefType(Long id) {
        return null;
    }

    @Override
    public UserDefType findById(Long id) {
        return null;
    }

    @Override
    public CommonResponse editUserDefType(UserDefTypeRequest userDefTypeRequest, Long id) {
        return null;
    }
}
