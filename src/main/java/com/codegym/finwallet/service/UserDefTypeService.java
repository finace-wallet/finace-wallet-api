package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;

public interface UserDefTypeService {
    CommonResponse updateWalletLimit(Long id, double newLimit);
    CommonResponse addWalletLimit(Long id, double additionalLimit);
}
