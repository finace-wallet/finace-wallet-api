package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.dto.WalletDto;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppUserRepository appUserRepository;
    @Override
    public WalletDto update(WalletDto walletDto) {
        Wallet wallet = modelMapper.map(walletDto, Wallet.class);
        wallet.setAppUser(appUserRepository.findById(walletDto.getId()).get());
        walletRepository.save(wallet);
        return walletDto;
    }

    @Override
    public void delete(Long id) {
        walletRepository.deleteById(id);
    }
}
