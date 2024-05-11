package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.AppUserSettingConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.AppUserSettingRequest;
import com.codegym.finwallet.entity.AppUserSetting;
import com.codegym.finwallet.repository.AppUserSettingRepository;
import com.codegym.finwallet.service.AppUserSettingService;
import com.codegym.finwallet.util.AuthUserExtractor;
import com.codegym.finwallet.util.BuildCommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserSettingServiceImpl implements AppUserSettingService {
    private final AppUserSettingRepository settingRepository;
    private final AuthUserExtractor authUserExtractor;
    private final BuildCommonResponse commonResponse;
    @Override
    public CommonResponse saveAppUserSetting(AppUserSettingRequest request) {
        Optional<AppUserSetting> appUserSettingOptional = settingRepository.findByAppUser_Email(authUserExtractor.getUsernameFromAuth());
        if (appUserSettingOptional.isPresent()) {
            AppUserSetting appUserSetting = appUserSettingOptional.get();
            appUserSetting.setType(request.getType());
            settingRepository.save(appUserSetting);
            return commonResponse.builResponse(null, AppUserSettingConstant.CHANGE_SETTING_SUCCESS, HttpStatus.OK);
        }
        return commonResponse.builResponse(null, AppUserSettingConstant.CHANGE_SETTING_FAIL, HttpStatus.BAD_REQUEST);
    }
}
