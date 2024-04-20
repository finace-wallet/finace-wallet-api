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

    String CHANGE_PASSWORD_SUCCESSFUL = "ChangePassword successfully";

    String USER_NOT_FOUND = "User not found";

    String DELETE_USER_SUCCESSFUL = "Delete user successfully";

    String USER_PROFILE_AVATAR_SUCCESS = "Update profile successful";

    String USER_PROFILE_AVATAR_FAIL_MESSAGE = "Image couldn't upload, Something went wrong!";

    String BUCKET_NAME = "fin-wallet-ee07d.appspot.com";

    String PRIVATE_KEY_FILE_NAME = "firebase-private-key.json";

    String CONTENT_TYPE_MEDIA = "media";

    String IMAGE_DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/fin-wallet-ee07d.appspot.com/o/%s?alt=media";
}
