package com.codegym.finwallet.constant;

public interface VarConstant {
    String ROLE_TYPE_USER = "USER";
    String ROLE_TYPE_ADMIN = "ADMIN";

    String AUTHORIZATION = "Authorization";

    String SUBJECT = "Mật khẩu mới của bạn là: ";

    int PASSWORD_MAX_LENGTH = 16;

    String MESSAGE_LOGIN_SUCCESS = "Login successful";

    String MESSAGE_LOGIN_FAIL = "Can't login with email: ";

    String FIND_PROFILE_FAIL = "Can't find profile: ";

    String UPDATE_PROFILE_AVATAR_SUCCESS = "Update profile successful";

    String UPDATE_PROFILE_AVATAR_FAIL = "Image couldn't upload, Something went wrong!";
}
