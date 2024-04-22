package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.WalletConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final AppUserRepository appUserRepository;

    @Override
    public Page<Wallet> findAllByEmail(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return walletRepository.findAllByEmail(pageable,email);
    }

    @Override
    public CommonResponse createWallet(WalletRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            AppUser appUser = appUserRepository.findByEmail(email);
            Wallet wallet = modelMapper.map(request,Wallet.class);
            wallet.setUsers(Collections.singletonList(appUser));
            walletRepository.save(wallet);
            return CommonResponse.builder()
                    .data(wallet)
                    .message(WalletConstant.CREATE_NEW_WALLET_SUCCESS_MESSAGE)
                    .status(HttpStatus.CREATED)
                    .build();
        }catch (AuthenticationException e){
            return CommonResponse.builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }


    @Override
    public CommonResponse deleteWallet(Long id) {
        try {
            Optional<Wallet> walletOptional = walletRepository.findById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            if (walletOptional.isPresent() && isUserWallet(id,email)) {
                Wallet wallet = walletOptional.get();
                wallet.setDelete(true);
                walletRepository.save(wallet);
                return CommonResponse.builder()
                        .data(null)
                        .message(WalletConstant.UPDATE_WALLET_INFORMATION_SUCCESS_MESSAGE)
                        .status(HttpStatus.OK)
                        .build();
        }
        }catch (SecurityException e){
            return CommonResponse.builder()
                    .data(null)
                    .message(WalletConstant.UPDATE_WALLET_INFORMATION_FAILURE_MESSAGE)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        return CommonResponse.builder()
                .data(null)
                .message(WalletConstant.UPDATE_WALLET_INFORMATION_DENIED)
                .status(HttpStatus.UNAUTHORIZED)
                .build();
    }

    @Override
    public Wallet findById(Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        Wallet newWallet = new Wallet();
        if (wallet.isPresent()){
             newWallet = wallet.get();
        }
        return newWallet;
    }

    @Override
    public CommonResponse editWallet(WalletRequest walletRequest,Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        try {
            Optional<Wallet> walletOptional = walletRepository.findById(id);
            if (!walletOptional.isPresent()) {
                return CommonResponse.builder()
                        .data(null)
                        .message(WalletConstant.WALLET_NOT_FOUND_MESSAGE)
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }
            if (isUserWallet(id, email)) {
                Wallet wallet = walletOptional.get();
                float currentAmount = wallet.getAmount();
                float inputAmount = walletRequest.getAmount();
                if (inputAmount < 0) {
                    return CommonResponse.builder()
                            .data(null)
                            .message(WalletConstant.AMOUNT_NOT_AVAILABLE)
                            .status(HttpStatus.BAD_REQUEST)
                            .build();
                }
                float newAmount = currentAmount + inputAmount;
                wallet.setAmount(newAmount);
                wallet.setIcon(walletRequest.getIcon());
                wallet.setName(walletRequest.getName());
                wallet.setDescription(walletRequest.getDescription());
                walletRepository.save(wallet);
                return CommonResponse.builder()
                        .data(wallet)
                        .message(WalletConstant.UPDATE_WALLET_INFORMATION_SUCCESS_MESSAGE)
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return CommonResponse.builder()
                        .data(null)
                        .message(WalletConstant.UPDATE_WALLET_INFORMATION_DENIED)
                        .status(HttpStatus.FORBIDDEN)
                        .build();
            }
        } catch (SecurityException e) {
            return CommonResponse.builder()
                    .data(null)
                    .message(WalletConstant.UPDATE_WALLET_INFORMATION_FAILURE_MESSAGE)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }

    private boolean isUserWallet(Long walletId, String email){
        List<Wallet> wallets = walletRepository.findWalletByEmail(email);
        Optional<Wallet> walletOptional = wallets.stream().filter(w -> w.getId().equals(walletId))
                .findFirst();
        return walletOptional.isPresent();
        }
}
