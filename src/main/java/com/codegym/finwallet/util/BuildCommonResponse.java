package com.codegym.finwallet.util;

import com.codegym.finwallet.dto.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BuildCommonResponse {
    public CommonResponse builResponse(Object data, String message, HttpStatus status) {
        return CommonResponse.builder()
                .data(data)
                .message(message)
                .status(status)
                .build();
    }
}
