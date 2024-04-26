package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.entity.TransactionHistory;
import com.codegym.finwallet.repository.TransactionRepository;
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
    @Override
    public CommonResponse create(TransactionRequest request) {
        TransactionHistory transactionHistory = modelMapper.map(request, TransactionHistory.class);
        transactionHistory.setSender(request.getSenderName());
        transactionHistory.setRecipient(request.getRecipientName());
        transactionHistory.setTransactionAmount(request.getTransactionAmount());
        transactionHistory.setDescription(request.getDescription());
        transactionRepository.save(transactionHistory);
        return CommonResponse.builder()
                .data(transactionHistory)
                .message("Saved transaction history")
                .status(HttpStatus.OK)
                .build();
    }
}
