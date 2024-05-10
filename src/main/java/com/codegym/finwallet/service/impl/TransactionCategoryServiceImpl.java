package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.TransactionCategoryConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionCategoryRequest;
import com.codegym.finwallet.dto.payload.response.TransactionCategoryResponse;
import com.codegym.finwallet.entity.TransactionCategory;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.TransactionCategoryRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.TransactionCategoryService;
import com.codegym.finwallet.util.BuildCommonResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionCategoryServiceImpl implements TransactionCategoryService {
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final WalletRepository walletRepository;
    private final BuildCommonResponse commonResponse;
    private final ModelMapper modelMapper;

    @Override
    public CommonResponse createTransactionCategory(TransactionCategoryRequest request, Long walletId) {
        TransactionCategory transactionCategory = modelMapper.map(request, TransactionCategory.class);
        Optional<Wallet> walletOptional = walletRepository.findById(walletId);
        if (walletOptional.isPresent()) {
            transactionCategory.setWallet(walletOptional.get());
            transactionCategoryRepository.save(transactionCategory);
            TransactionCategoryResponse response = modelMapper.map(transactionCategory, TransactionCategoryResponse.class);
            return commonResponse.builResponse(response, TransactionCategoryConstant.CREATE_TRANSACTION_CATEGORY_SUCCESS, HttpStatus.CREATED);
        }
        return commonResponse.builResponse(null,TransactionCategoryConstant.CREATE_TRANSACTION_CATEGORY_FAILURE,HttpStatus.BAD_REQUEST);
    }
}
