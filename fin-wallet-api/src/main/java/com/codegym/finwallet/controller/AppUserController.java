package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.UpdateProfileRequest;
import com.codegym.finwallet.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class AppUserController {

    private final ProfileService profileService;

    @PutMapping("/update-profile")
    public ResponseEntity<CommonResponse> updateProfile(@RequestBody UpdateProfileRequest request){
        CommonResponse commonResponse = profileService.UpdateProfile(request);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
