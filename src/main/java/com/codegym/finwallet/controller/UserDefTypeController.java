package com.codegym.finwallet.controller;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.UserDefTypeRequest;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.service.UserDefTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/userDefTypes")
@RequiredArgsConstructor
public class UserDefTypeController {
    private final UserDefTypeService userDefTypeService;
    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createUserDefType(@RequestBody UserDefTypeRequest request) {
        CommonResponse commonResponse = userDefTypeService.createUserDefType(request);
        return ResponseEntity.status(commonResponse.getStatus()).body(commonResponse);
    }
}
