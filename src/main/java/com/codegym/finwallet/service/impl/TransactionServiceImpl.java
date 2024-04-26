package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.entity.Transaction;
import com.codegym.finwallet.repository.TransactionRepository;
import com.codegym.finwallet.repository.WalletTransactionRepository;
import com.codegym.finwallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final WalletTransactionRepository walletTransactionRepository;
    @Override
    public CommonResponse create(TransactionRequest request) {
        Transaction transaction = modelMapper.map(request, Transaction.class);
        transactionRepository.save(transaction);
        return CommonResponse.builder()
                .data(transaction)
                .message("Saved transaction history")
                .status(HttpStatus.OK)
                .build();
    }
}
