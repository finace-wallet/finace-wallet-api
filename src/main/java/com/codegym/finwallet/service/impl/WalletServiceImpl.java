package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.WalletService;
import com.google.api.Http;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Wallet save(WalletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser appUser = appUserRepository.findByEmail(email);
        List<AppUser> appUsers = new ArrayList<>();
        appUsers.add(appUser);
        Wallet wallet = modelMapper.map(request,Wallet.class);
        wallet.setUsers(appUsers);
        return walletRepository.save(wallet);
    }


    @Override
    public Wallet edit(Long id) {
        return walletRepository.save(walletRepository.findById(id).get());
    }

    @Override
    public void remove(Long id) {
        walletRepository.deleteById(id);
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
    public CommonResponse editWallet(Long id, WalletRequest walletRequest) {
        try{
            Wallet wallet = findById(id);
            float curentAmount = wallet.getAmount();
            float inputAmound = walletRequest.getAmount();
            float newAmount = curentAmount + inputAmound;
            wallet.setAmount(newAmount);
            wallet.setIcon(walletRequest.getIcon());
            wallet.setName(walletRequest.getName());
            wallet.setDescription(walletRequest.getDescription());
            walletRepository.save(wallet);

            return CommonResponse.builder()
                    .data(wallet)
                    .message("Thành công!")
                    .status(HttpStatus.OK)
                    .build();
        }catch (SecurityException e){
            return CommonResponse.builder()
                    .data(null)
                    .message("Thất bại")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

    }


}
