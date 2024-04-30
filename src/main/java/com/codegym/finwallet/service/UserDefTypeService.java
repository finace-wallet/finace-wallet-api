package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.UserDefTypeRequest;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.entity.UserDefType;
import com.codegym.finwallet.entity.Wallet;

public interface UserDefTypeService {

    CommonResponse createUserDefType(UserDefTypeRequest request);
    CommonResponse deleteUserDefType(Long id);
    UserDefType findById(Long id);
    CommonResponse editUserDefType(UserDefTypeRequest userDefTypeRequest,Long id);

}
