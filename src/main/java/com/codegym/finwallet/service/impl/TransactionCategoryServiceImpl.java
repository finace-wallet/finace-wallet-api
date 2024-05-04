package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.TransactionConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionCategoryRequest;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.TransactionCategory;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.TransactionCategoryRepository;
import com.codegym.finwallet.service.TransactionCategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionCategoryServiceImpl implements TransactionCategoryService {
    private final TransactionCategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final AppUserRepository appUserRepository;
    @Override
    public CommonResponse createTransactionCategory(TransactionCategoryRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            AppUser appUser = appUserRepository.findByEmail(email);
            TransactionCategory transactionCategory = modelMapper.map(request, TransactionCategory.class);
            transactionCategory.setAppUser(appUser);
            categoryRepository.save(transactionCategory);
            return buildResponse(transactionCategory, TransactionConstant.CREATE_TRANSACTION_CATEGORY_SUCCESSFUL, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            return buildResponse(null, e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public Page<TransactionCategory> findAllTransactionCategoryByEmail(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return categoryRepository.findAllByEmail(pageable, email);
    }

    private CommonResponse buildResponse(Object data, String message, HttpStatus status) {
        return CommonResponse.builder()
                .data(data)
                .message(message)
                .status(status)
                .build();
    }
}
