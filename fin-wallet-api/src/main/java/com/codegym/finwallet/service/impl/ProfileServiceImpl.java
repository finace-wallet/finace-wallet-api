package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.VarConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.UpdateProfileRequest;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.repository.ProfileRepository;
import com.codegym.finwallet.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private String FIND_PROFILE_FAILE_MESSAGE = VarConstant.FIND_PROFILE_FAIL;
    private final ModelMapper modelMapper;

    @Override
    public Profile findProfileByEmail(String email) {
        Optional<Profile> profile = profileRepository.findProfileByEmail(email);
        return  profile.orElseThrow(() -> new RuntimeException(FIND_PROFILE_FAILE_MESSAGE + email));
    }

    @Override
    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    @Override
    public CommonResponse UpdateProfile(UpdateProfileRequest request) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Profile curentProfile = findProfileByEmail(email);
            Profile newProfile = modelMapper.map(request, curentProfile.getClass());
            newProfile.setId(curentProfile.getId());
            profileRepository.save(newProfile);
            commonResponse = commonResponse.builder()
                    .data(null)
                    .message("Update profile successful")
                    .status(HttpStatus.OK)
                    .build();
        }catch (SecurityException e){
            commonResponse = commonResponse.builder()
                    .data(null)
                    .message("Can't update profile")
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        return commonResponse;
    }
}
