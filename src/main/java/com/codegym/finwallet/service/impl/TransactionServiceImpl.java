package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.TransactionConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.dto.payload.response.TransactionResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.entity.Transaction;
import com.codegym.finwallet.entity.TransactionCategory;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.ProfileRepository;
import com.codegym.finwallet.repository.TransactionCategoryRepository;
import com.codegym.finwallet.repository.TransactionRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.TransactionService;
import com.codegym.finwallet.util.AuthUserExtractor;
import com.codegym.finwallet.util.BuildCommonResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final AuthUserExtractor userExtractor;
    private final AppUserRepository appUserRepository;
    private final WalletRepository walletRepository;
    private final BuildCommonResponse commonResponse;
    private final ProfileRepository profileRepository;
    @Override
    public CommonResponse saveTransaction(TransactionRequest request, Long walletId) {
        String email = userExtractor.getUsernameFromAuth();
        TransactionCategory transactionCategory = getTransactionCategory(request.getTransactionCategoryId());
        AppUser appUser = getUser(email);
        Wallet wallet = getWallet(walletId);
        if (transactionCategory != null && appUser != null && wallet != null) {
            Transaction transaction = buildTransaction(request,appUser,wallet,transactionCategory);
            TransactionResponse transactionResponse = buildResponse(transaction,email);

            return commonResponse.builResponse(transactionResponse, TransactionConstant.CREATE_TRANSACTION_SUCCESSFUL, HttpStatus.CREATED);
        }
        return commonResponse.builResponse(null, TransactionConstant.CREATE_TRANSACTION_FAILED, HttpStatus.BAD_REQUEST);
    }

    @Override
    public CommonResponse findAllTransactionsByWalletId(Long walletId, Pageable pageable) {
        String email = userExtractor.getUsernameFromAuth();
        Page<Transaction> transactions = transactionRepository.findAllByWalletId(walletId,pageable);
        List<TransactionResponse> responses = transactions.stream()
                .map(transaction -> buildResponse(transaction,email))
                .toList();
        PageImpl<TransactionResponse> page = new PageImpl<>(responses,pageable,transactions.getTotalElements());
        return commonResponse.builResponse(page,TransactionConstant.FIND_TRANSACTION_SUCCESSFUL,HttpStatus.OK);
    }

    @Override
    public CommonResponse findAllTransactionsByCategory( Long transactionCategoryId, Pageable pageable) {
        return null;
    }

    private TransactionCategory getTransactionCategory(Long id) {
        Optional<TransactionCategory> transactionCategory = transactionCategoryRepository.findById(id);
        return transactionCategory.orElse(null);
    }

    private AppUser getUser(String email){
        Optional<AppUser> appUserOptional = appUserRepository.findAppUserByEmail(email);
        return appUserOptional.orElse(null);
    }

    private Wallet getWallet(Long walletId){
        Optional<Wallet> walletOptional = walletRepository.findById(walletId);
        return walletOptional.orElse(null);
    }

    private Transaction buildTransaction(TransactionRequest request,AppUser appUser,Wallet wallet, TransactionCategory transactionCategory){
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setTransactionCategory(transactionCategory);
        transaction.setAppUser(appUser);
        transaction.setWallet(wallet);
        transactionRepository.save(transaction);
        return transaction;
    }

    private TransactionResponse convertToResponse(Transaction transaction){
        return modelMapper.map(transaction, TransactionResponse.class);
    }

    private Profile getProfile(String email) {
        return profileRepository.findProfileByEmail(email).orElse(null);
    }

    private TransactionResponse buildResponse(Transaction transaction, String email){
        Profile profile = getProfile(email);

        TransactionResponse transactionResponse = convertToResponse(transaction);
        transactionResponse.setFullName(profile.getFullName());
        transactionResponse.setWalletName(transaction.getWallet().getName());
        transactionResponse.setCategoryName(transaction.getTransactionCategory().getName());
        transactionResponse.setType(transaction.getTransactionCategory().getType());

        return transactionResponse;
    }
}
