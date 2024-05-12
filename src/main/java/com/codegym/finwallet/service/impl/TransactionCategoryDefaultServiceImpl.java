package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.TransactionCategoryConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.response.TransactionResponse;
import com.codegym.finwallet.entity.TransactionCategoryDefault;
import com.codegym.finwallet.repository.TransactionCategoryDefaultRepository;
import com.codegym.finwallet.service.TransactionCategoryDefaultService;
import com.codegym.finwallet.util.BuildCommonResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionCategoryDefaultServiceImpl implements TransactionCategoryDefaultService {
    private final TransactionCategoryDefaultRepository transactionCategoryDefaultRepository;
    private final ModelMapper modelMapper;
    private final BuildCommonResponse commonResponse;

    @Override
    public CommonResponse findAllDefaultCategory() {
        List<TransactionCategoryDefault> transactionCategoryDefaults = transactionCategoryDefaultRepository.findAll();
        List<TransactionResponse> transactionResponses = transactionCategoryDefaults.stream()
                .map(transactionCategoryDefault -> modelMapper.map(transactionCategoryDefault, TransactionResponse.class))
                .collect(Collectors.toList());
    return commonResponse.builResponse(transactionResponses, TransactionCategoryConstant.GET_LIST_TRANSACTION_CATEGORY_SUCCESS, HttpStatus.OK);
    }
}
