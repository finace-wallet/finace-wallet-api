package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.TransactionCategoryConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionCategoryRequest;
import com.codegym.finwallet.dto.payload.response.TransactionCategoryResponse;
import com.codegym.finwallet.entity.TransactionCategory;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.TransactionCategoryRepository;
import com.codegym.finwallet.repository.TransactionRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.TransactionCategoryService;
import com.codegym.finwallet.util.AuthUserExtractor;
import com.codegym.finwallet.util.BuildCommonResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public CommonResponse getAllCategoryTypeIncome(Long walletId) {
        List<TransactionCategory> transactionCategories = transactionCategoryRepository.findAllByWalletIdAndType(walletId, TransactionCategoryConstant.CATEGORY_TYPE_INCOME);
        List<TransactionCategoryResponse> responses = transactionCategories.stream()
                .map(transactionCategory -> modelMapper.map(transactionCategory,TransactionCategoryResponse.class))
                .collect(Collectors.toList());
       return commonResponse.builResponse(responses,TransactionCategoryConstant.GET_LIST_TRANSACTION_CATEGORY_SUCCESS,HttpStatus.OK);
    }

    @Override
    public CommonResponse getAllCategoryTypeExpense(Long walletId) {
        List<TransactionCategory> transactionCategories = transactionCategoryRepository.findAllByWalletIdAndType(walletId, TransactionCategoryConstant.CATEGORY_TYPE_EXPENSE);
        List<TransactionCategoryResponse> responses = transactionCategories.stream()
                .map(transactionCategory -> modelMapper.map(transactionCategory,TransactionCategoryResponse.class))
                .collect(Collectors.toList());
        return commonResponse.builResponse(responses,TransactionCategoryConstant.GET_LIST_TRANSACTION_CATEGORY_SUCCESS,HttpStatus.OK);
    }

    @Override
    public CommonResponse getAllCategory(Long walletId){
        List<TransactionCategory> transactionCategories = transactionCategoryRepository.findAllByWalletId(walletId);
        List<TransactionCategoryResponse> responses = transactionCategories.stream().map(transactionCategory ->
                modelMapper.map(transactionCategory,TransactionCategoryResponse.class)).collect(Collectors.toList());
        return  commonResponse.builResponse(responses,null,HttpStatus.OK);
    }

    @Override
    public CommonResponse createBudget(Long walletId, Long categoryId, double budget) {
        Optional<TransactionCategory> categoryOptional = transactionCategoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            TransactionCategory transactionCategory = categoryOptional.get();
            transactionCategory.setBudget(budget);
            transactionCategoryRepository.save(transactionCategory);
            return commonResponse.builResponse(transactionCategory, TransactionCategoryConstant.CREATE_SUCCESSFUL_BUDGET, HttpStatus.CREATED);
        }
       return null;
    }
}
