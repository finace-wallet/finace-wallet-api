package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.AuthConstant;
import com.codegym.finwallet.constant.UserConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.ProfileRequest;
import com.codegym.finwallet.dto.payload.response.ProfileResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.repository.AppUserRepository;
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
    private final ModelMapper modelMapper;
    private final AppUserRepository appUserRepository;

    @Override
    public Profile findProfileByEmail(String email) {
        Optional<Profile> profile = profileRepository.findProfileByEmail(email);
        return profile.orElse(null);
    }

    private Profile findProfileByAppUser_Id(Long userId) {
        Optional<Profile> profileOptional = profileRepository.findProfileByAppUser_Id(userId);
        if (profileOptional.isPresent()) {
            return profileOptional.get();
        } else {
            // Handle case where no profile is found for the given user id
            System.out.println("Something is wrong");
            return null;
        }
    }

    @Override
    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }

    @Override
    public CommonResponse updateProfile(ProfileRequest request) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
//            Profile curentProfile = findProfileByEmail(email);
            AppUser appUser = appUserRepository.findByEmail(email);
            Profile currentProfile = findProfileByAppUser_Id(appUser.getId());

            Profile newProfile = modelMapper.map(request, currentProfile.getClass());
            newProfile.setId(currentProfile.getId());
            newProfile.setAppUser(appUser);

            profileRepository.save(newProfile);
            commonResponse = commonResponse.builder()
                    .data(null)
                    .message(UserConstant.UPDATE_PROFILE_SUCCESSFUL_MESSAGE)
                    .status(HttpStatus.OK)
                    .build();
        }catch (SecurityException e){
            commonResponse = commonResponse.builder()
                    .data(null)
                    .message(UserConstant.UPDATE_PROFILE_FAIL_MESSAGE)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        return commonResponse;
    }

    @Override
    public CommonResponse getProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Profile profile = findProfileByEmail(email);
            ProfileResponse profileResponse = modelMapper.map(profile, ProfileResponse.class);
            return CommonResponse.builder()
                    .data(profileResponse)
                    .message(UserConstant.FIND_PROFILE_SUCCESSFUL_MESSAGE)
                    .status(HttpStatus.OK)
                    .build();
        }catch (SecurityException e){
            return CommonResponse.builder()
                    .data(null)
                    .message(UserConstant.FIND_PROFILE_FAIL_MESSAGE)
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }
}
