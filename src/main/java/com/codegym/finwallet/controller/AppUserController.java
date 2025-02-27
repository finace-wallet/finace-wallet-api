package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.AppUserSettingRequest;
import com.codegym.finwallet.dto.payload.request.ProfileRequest;
import com.codegym.finwallet.service.AppUserSettingService;
import com.codegym.finwallet.service.ImageService;
import com.codegym.finwallet.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class AppUserController {

    private final ProfileService profileService;
    private final ImageService imageService;
    private final AppUserSettingService settingService;

    @PutMapping("/profile")
    public ResponseEntity<CommonResponse> updateProfile(@RequestBody ProfileRequest request){
        CommonResponse commonResponse = profileService.updateProfile(request);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @PatchMapping("/profile/avatar")
    public ResponseEntity<CommonResponse> updateProfileAvatar(@RequestBody MultipartFile file){
        CommonResponse commonResponse = imageService.upload(file);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<CommonResponse> getProfile(){
        CommonResponse commonResponse = profileService.getProfile();
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @PatchMapping("/setting/change")
    public ResponseEntity<CommonResponse> changeTimeSendEmail(@RequestBody AppUserSettingRequest request){
        CommonResponse commonResponse = settingService.saveAppUserSetting(request);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
