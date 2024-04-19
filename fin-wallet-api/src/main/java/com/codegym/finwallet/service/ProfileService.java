package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.UpdateProfileRequest;
import com.codegym.finwallet.entity.Profile;

public interface ProfileService {
    Profile findProfileByEmail(String email);

    void saveProfile(Profile profile);

    CommonResponse UpdateProfile(UpdateProfileRequest request);
}
