package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.AppUserSettingRequest;
import com.codegym.finwallet.entity.AppUserSetting;

public interface AppUserSettingService {
    CommonResponse saveAppUserSetting(AppUserSettingRequest request);
}
