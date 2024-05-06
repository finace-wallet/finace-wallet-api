package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.SpendingRequest;

public interface SpendingService {
    CommonResponse addSpending(SpendingRequest spendingRequest);
}
