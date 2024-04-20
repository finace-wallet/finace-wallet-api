package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.UpdateProfileRequest;
import com.codegym.finwallet.service.ImageService;
import com.codegym.finwallet.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class AppUserController {

    private final ProfileService profileService;
    private final ImageService imageService;

    @PutMapping("/update-profile")
    public ResponseEntity<CommonResponse> updateProfile(@RequestBody UpdateProfileRequest request){
        CommonResponse commonResponse = profileService.UpdateProfile(request);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }

    @PatchMapping("/update-profile/avatar")
    public ResponseEntity<CommonResponse> updateProfileAvatar(@RequestBody MultipartFile file){
        CommonResponse commonResponse = imageService.upload(file);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
