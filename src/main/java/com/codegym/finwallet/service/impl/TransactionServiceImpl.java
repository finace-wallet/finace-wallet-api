package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.TransactionConstant;
import com.codegym.finwallet.constant.WalletConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;
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

import java.time.LocalDate;
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
        boolean isExpense = isTransactionCateGoryExpense(transactionCategory);
        AppUser appUser = getUser(email);
        Wallet wallet = getWallet(walletId);
        if (transactionCategory != null && appUser != null && wallet != null) {
            Transaction transaction = buildTransaction(request,appUser,wallet,transactionCategory,isExpense);
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
    public CommonResponse findAllTransactionsByCategory(Long walletId, Long transactionCategoryId, Pageable pageable) {
        String email = userExtractor.getUsernameFromAuth();
        Page<Transaction> transactions = transactionRepository.findAllByWalletIdAndTransactionCategoryId(walletId,transactionCategoryId,pageable);
        List<TransactionResponse> responses = transactions.stream()
                .map(transaction -> buildResponse(transaction,email))
                .toList();
        PageImpl<TransactionResponse> page = new PageImpl<>(responses,pageable,transactions.getTotalElements());
        return commonResponse.builResponse(page,TransactionConstant.FIND_TRANSACTION_SUCCESSFUL,HttpStatus.OK);
    }

    @Override
    public CommonResponse deleteTransaction(Long transactionId) {
        Transaction transaction = getTransaction(transactionId);
        if (transaction != null) {
            transaction.setDelete(true);
            transactionRepository.save(transaction);
            return commonResponse.builResponse(null,TransactionConstant.DELETE_TRANSACTION_SUCCESSFUL,HttpStatus.OK);
        }
        return commonResponse.builResponse(null,TransactionConstant.DELETE_TRANSACTION_FAILED,HttpStatus.BAD_REQUEST);
    }

    @Override
    public CommonResponse transferMoney(TransferMoneyRequest request, Long walletId) {
        try {
            Wallet receiverWallet = plusMoney(request);
            Wallet senderWallet = deductMoney(walletId,request);
            String email = userExtractor.getUsernameFromAuth();
            AppUser appUser = getUser(email);
            Transaction senderTransaction = buildTransactionForDeductWallet(request,appUser,senderWallet);
            Transaction receiverTransaction = buildTransactionForReceiverWallet(request,appUser,receiverWallet);

            TransactionResponse transactionResponse = modelMapper.map(senderTransaction,TransactionResponse.class);
            transactionResponse.setFullName(getProfile(email).getFullName());
            return commonResponse.builResponse(transactionResponse, WalletConstant.TRANSFER_MONEY_SUCCESS,HttpStatus.CREATED);
        }catch (Exception e){
            return commonResponse.builResponse(null,WalletConstant.TRANSFER_MONEY_FAILED, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public CommonResponse editTransaction(TransactionRequest request, Long walletId, Long transactionId) {
        if (isTransactionInWallet(walletId, transactionId)) {
            Transaction transaction = getTransaction(transactionId);
            if (transaction != null) {
                transaction = modelMapper.map(request,Transaction.class);
                transactionRepository.save(transaction);
                TransactionResponse transactionResponse = modelMapper.map(transaction,TransactionResponse.class);
                return commonResponse.builResponse(transactionResponse,TransactionConstant.EDIT_TRANSACTION_SUCCESS,HttpStatus.OK);
            }
        }
        return commonResponse.builResponse(null,TransactionConstant.FIND_TRANSACTION_FAILED,HttpStatus.BAD_REQUEST);
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

    private Transaction buildTransaction(TransactionRequest request,AppUser appUser,Wallet wallet,
                                         TransactionCategory transactionCategory, boolean isExpense){
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setTransactionCategory(transactionCategory);
        transaction.setAppUser(appUser);
        transaction.setWallet(wallet);
        transaction.setExpense(isExpense);
        transaction.setCurrency(request.getCurrency());
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

    private Transaction getTransaction(Long transactionId){
        return transactionRepository.findById(transactionId).orElse(null);
    }

    private Wallet plusMoney(TransferMoneyRequest request){
        Wallet wallet = getWallet(request.getDestinationWalletId());
        double inputAmount = request.getAmount();
        double currentAmount = wallet.getAmount();
        double newAmount = currentAmount + inputAmount;
        wallet.setAmount(newAmount);
        walletRepository.save(wallet);
        return wallet;
    }

    private Wallet deductMoney(Long walletId, TransferMoneyRequest request){
        Wallet wallet = getWallet(walletId);
        double currentAmount = wallet.getAmount();
        double newAmount = currentAmount - request.getAmount();
        wallet.setAmount(newAmount);
        walletRepository.save(wallet);
        return wallet;
    }

    private Transaction buildTransactionForDeductWallet(TransferMoneyRequest request, AppUser appUser, Wallet wallet){
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransfer(true);
        transaction.setExpense(true);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setAppUser(appUser);
        transaction.setWallet(wallet);

        transactionRepository.save(transaction);
        return transaction;
    }

    private Transaction buildTransactionForReceiverWallet(TransferMoneyRequest request, AppUser appUser, Wallet wallet){
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransfer(true);
        transaction.setExpense(false);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setAppUser(appUser);
        transaction.setWallet(wallet);

        transactionRepository.save(transaction);
        return transaction;
    }

    private boolean isTransactionCateGoryExpense(TransactionCategory transactionCategory){
        if (transactionCategory.getType().equals("EXPENSE")){
            return true;
        }
        return false;
    }

    private boolean isTransactionInWallet(Long transactionId, Long walletId){
        Wallet wallet = getWallet(walletId);
        Transaction transaction = getTransaction(transactionId);

        if (transaction!= null && wallet != null ){
            return true;
        }
        return false;
    }

}
