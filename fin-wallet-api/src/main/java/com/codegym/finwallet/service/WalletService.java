package com.codegym.finwallet.service;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WalletService {
    Page<Wallet> findAllByEmail(Pageable pageable);
    Wallet save(WalletRequest request);
    void remove(Long id);
    Wallet findById(Long id);

}
