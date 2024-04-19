package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.dto.WalletDto;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        Wallet wallet = modelMapper.map(request,Wallet.class);
        return walletRepository.save(wallet);
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


}
