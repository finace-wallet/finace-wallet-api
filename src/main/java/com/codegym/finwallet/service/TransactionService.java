package com.codegym.finwallet.service;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;

public interface TransactionService {
//    CommonResponse create(TransactionRequest request);

    CommonResponse transferMoney(TransferMoneyRequest transferMoneyRequest);
}
