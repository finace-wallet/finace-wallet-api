package com.codegym.finwallet.converter;

import com.codegym.finwallet.dto.payload.response.WalletResponse;
import com.codegym.finwallet.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletConvert {
    private final ModelMapper modelMapper;

    public WalletResponse convertToWalletResponse(Wallet wallet) {
        return modelMapper.map(wallet, WalletResponse.class);
    }
}
