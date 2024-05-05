package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.WalletConstant;
import com.codegym.finwallet.constant.WalletOwnershipConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.dto.payload.response.WalletResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.entity.WalletOwnership;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.WalletOwnershipRepository;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final AppUserRepository appUserRepository;
    private final WalletOwnershipRepository walletOwnershipRepository;

    @Override
    public Page<Wallet> findAllByEmail(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return walletRepository.findAllByEmail(pageable, email);
    }

    @Override
    public CommonResponse createWallet(WalletRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            AppUser appUser = appUserRepository.findByEmail(email);
//            Wallet wallet = modelMapper.map(request, Wallet.class);
//            wallet.setUsers(Collections.singletonList(appUser));

            Wallet wallet = new Wallet();
            wallet.setName(request.getName());
            wallet.setCurrentType(request.getCurrentType());
            wallet.setDescription(request.getDescription());
            wallet.setAmount(request.getAmount());
            walletRepository.save(wallet);

            WalletOwnership ownership = new WalletOwnership();
            ownership.setOwnership(WalletOwnershipConstant.OWNERSHIP_OWNER);
            ownership.setWallet(wallet);
            ownership.setAppUser(appUser);
            walletOwnershipRepository.save(ownership);


            WalletResponse response = modelMapper.map(wallet, WalletResponse.class);
            return buildResponse(response,WalletConstant.CREATE_NEW_WALLET_SUCCESS_MESSAGE, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            return buildResponse(null,e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    //chua fix
    @Override
    public CommonResponse deleteWallet(Long id) {
        try {
            Optional<Wallet> walletOptional = walletRepository.findById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            if (walletOptional.isPresent() && isUserWallet(id, email)) {
                Wallet wallet = walletOptional.get();
                wallet.setDelete(true);
                walletRepository.save(wallet);
                return buildResponse(null,WalletConstant.UPDATE_WALLET_INFORMATION_SUCCESS_MESSAGE, HttpStatus.OK);
            }
        } catch (SecurityException e) {
            return buildResponse(null,WalletConstant.UPDATE_WALLET_INFORMATION_FAILURE_MESSAGE, HttpStatus.UNAUTHORIZED);
        }
        return buildResponse(null,WalletConstant.UPDATE_WALLET_INFORMATION_DENIED,HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Wallet findById(Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        Wallet newWallet = new Wallet();
        if (wallet.isPresent()) {
            newWallet = wallet.get();
        }
        return newWallet;
    }

    @Override
    public CommonResponse editWallet(WalletRequest walletRequest, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        try {
            Optional<Wallet> walletOptional = walletRepository.findById(id);
            if (!walletOptional.isPresent()) {

                return buildResponse(null,WalletConstant.WALLET_NOT_FOUND_MESSAGE,HttpStatus.OK);
            }
            if (isUserWallet(id, email)) {
                Wallet wallet = walletOptional.get();
                double currentAmount = wallet.getAmount();
                double inputAmount = walletRequest.getAmount();
                if (inputAmount < 0) {
                    return buildResponse(null,WalletConstant.AMOUNT_NOT_AVAILABLE,HttpStatus.BAD_REQUEST);
                }
                double newAmount = currentAmount + inputAmount;
                wallet.setAmount(newAmount);
//                wallet.setIcon(walletRequest.getIcon());
                wallet.setName(walletRequest.getName());
                wallet.setDescription(walletRequest.getDescription());
                walletRepository.save(wallet);
                WalletResponse walletResponse = modelMapper.map(wallet,WalletResponse.class);
                return buildResponse(walletResponse,WalletConstant.UPDATE_WALLET_INFORMATION_SUCCESS_MESSAGE,HttpStatus.OK);
            } else {
                return buildResponse(null,WalletConstant.UPDATE_WALLET_INFORMATION_DENIED,HttpStatus.FORBIDDEN);
            }
        } catch (SecurityException e) {
            return buildResponse(null,WalletConstant.UPDATE_WALLET_INFORMATION_FAILURE_MESSAGE,HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isUserWallet(Long walletId, String email) {
        List<Wallet> wallets = walletRepository.findWalletByEmail(email);
        Optional<Wallet> walletOptional = wallets.stream().filter(w -> w.getId().equals(walletId))
                .findFirst();
        return walletOptional.isPresent();
    }

    private CommonResponse buildResponse(Object data, String message, HttpStatus status) {
        return CommonResponse.builder()
                .data(data)
                .message(message)
                .status(status)
                .build();
    }


    @Override
    public CommonResponse addMoneyToWallet(Long walletId, double amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        List<Wallet> wallets = walletRepository.findWalletByEmail(userEmail);

        Optional<Wallet> walletOptional = wallets.stream().filter(wallet -> wallet.getId().equals(walletId)).findFirst();
        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            double currentAmount = wallet.getAmount();
            wallet.setAmount(currentAmount + amount);
            walletRepository.save(wallet);
            return buildResponse(null, WalletConstant.MONEY_ADDED_SUCCESSFULLY, HttpStatus.OK);
        } else {
            return buildResponse(null, WalletConstant.WALLET_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
        }
    }
}
