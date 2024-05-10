package com.codegym.finwallet.converter;

import com.codegym.finwallet.dto.payload.response.TransactionCategoryResponse;
import com.codegym.finwallet.entity.TransactionCategory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionCategoryConvert {
    private final ModelMapper modelMapper;

    public TransactionCategoryResponse convertToResponse(TransactionCategory transactionCategory) {
        return modelMapper.map(transactionCategory, TransactionCategoryResponse.class);
    }
}
