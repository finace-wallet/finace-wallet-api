package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.SpendingConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.SpendingRequest;
import com.codegym.finwallet.dto.payload.response.SpendingResponse;
import com.codegym.finwallet.dto.payload.response.WalletResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Spending;
import com.codegym.finwallet.entity.UserDefType;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.SpendingRepository;
import com.codegym.finwallet.repository.UserDefTypeRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.SpendingService;
import com.google.api.gax.rpc.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SpendingServiceImpl implements SpendingService {
    private final SpendingRepository spendingRepository;
    private final WalletRepository walletRepository;
    private final UserDefTypeRepository userDefTypeRepository;
    private final ModelMapper modelMapper;
    private final AppUserRepository appUserRepository;
    @Override
    public CommonResponse addSpending(SpendingRequest spendingRequest) {
            if (!isWalletExist(spendingRequest.getWallet_id()) && !isUserDefTypeExist(spendingRequest.getUserDefType_id())) {
                Spending spending = Spending.builder()
                        .amount(spendingRequest.getAmount())
                        .note(spendingRequest.getNote())
                        .userDefType(getUserDefType(spendingRequest.getWallet_id()))
                        .wallet(getWallet(spendingRequest.getWallet_id()))
                        .build();

                SpendingResponse spendingResponse = modelMapper.map(spending, SpendingResponse.class);
                spendingResponse.setUserDefTypeName(spending.getUserDefType().getName());
                spendingResponse.setWalletName(spending.getWallet().getName());
                return buildResponse(spendingResponse, SpendingConstant.SPENDING_ADD_SUCCESS, HttpStatus.OK);
            }
        return buildResponse(null, SpendingConstant.SPENDING_ADD_FAILED, HttpStatus.BAD_REQUEST);
    }

    private boolean isWalletExist(Long id){
        try{
        Wallet wallet = walletRepository.findById(id).get();
        return wallet.isDelete();
        }catch (Exception e){
            return false;
        }
    }

    private boolean isUserDefTypeExist(Long id){
        try{
            UserDefType userDefType = userDefTypeRepository.findById(id).get();
            return userDefType.isDelete();
        }catch (Exception e){
            return false;
        }
    }
    private CommonResponse buildResponse(Object data, String message, HttpStatus status){
        return CommonResponse.builder()
                .data(data)
                .message(message)
                .status(status)
                .build();
    }

    private Wallet getWallet(Long id){
        return walletRepository.findById(id).get();
    }

    private UserDefType getUserDefType(Long id){
        return userDefTypeRepository.findById(id).get();
    }

    public boolean checkWalletOfUser(Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser user = appUserRepository.findByEmail(email);
        for(Wallet wallet : user.getWallets()){
            if(wallet.getId() == id){
                return true;
            }
        }
        return false;
    }
}
