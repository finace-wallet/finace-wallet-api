package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.TransactionConstant;
import com.codegym.finwallet.constant.WalletConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;
import com.codegym.finwallet.dto.payload.response.TransactionResponse;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.entity.Transaction;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.entity.WalletTransaction;
import com.codegym.finwallet.repository.ProfileRepository;
import com.codegym.finwallet.repository.TransactionRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.repository.WalletTransactionRepository;
import com.codegym.finwallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final ProfileRepository profileRepository;

    @Override
    public CommonResponse saveTransaction(TransactionRequest request,TransferMoneyRequest transferMoneyRequest) {
        Transaction transaction = modelMapper.map(request, Transaction.class);
        transactionRepository.save(transaction);

        WalletTransaction senderWalletTransaction = new WalletTransaction();
        senderWalletTransaction.setTransaction(transaction);
        senderWalletTransaction.setWallet(getSourceWallet(transferMoneyRequest));
        walletTransactionRepository.save(senderWalletTransaction);

        WalletTransaction receiverWalletTransaction = new WalletTransaction();
        receiverWalletTransaction.setTransaction(transaction);
        receiverWalletTransaction.setWallet(getDestinationWallet(transferMoneyRequest));
        walletTransactionRepository.save(receiverWalletTransaction);

        TransactionResponse transactionResponse = modelMapper.map(transaction,TransactionResponse.class);
        return buildResponse(transactionResponse,TransactionConstant.SAVE_TRANSACTION_SUCCESSFUL,HttpStatus.OK);
    }
    @Override
    public CommonResponse transferMoney(TransferMoneyRequest transferMoneyRequest) {
        String sourceEmail = getAuthenticatedEmail();
        Optional<Profile> sourceProfile = profileRepository.findProfileByEmail(sourceEmail);
        Optional<Profile> destinationProfile = profileRepository.findProfileByEmail(transferMoneyRequest.getDestinationEmail());

        if (sourceProfile.isEmpty() || destinationProfile.isEmpty()) {
            return buildResponse(null, WalletConstant.PROFILE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        Wallet sourceWallet = getSourceWallet(transferMoneyRequest);
        Wallet destinationWallet = getDestinationWallet(transferMoneyRequest);

        if (sourceWallet == null || destinationWallet == null) {
            return buildResponse(null, WalletConstant.WALLET_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
        }

        double amount = transferMoneyRequest.getAmount();
        return processTransferAndUpdateTransaction(sourceProfile.get(), destinationProfile.get(), sourceWallet, destinationWallet, amount, transferMoneyRequest);
    }

    @Override
    public CommonResponse findAllTransactionsByWalletId(Pageable pageable,Long walletID) {
        Page<Transaction> transactions = transactionRepository.findAllByWalletId(pageable,walletID);
        return buildResponse(transactions,"",HttpStatus.OK);
    }

    private CommonResponse processTransferAndUpdateTransaction(Profile sourceProfile, Profile destinationProfile, Wallet sourceWallet, Wallet destinationWallet, double amount, TransferMoneyRequest transferMoneyRequest) {
        CommonResponse transferResponse = processTransfer(sourceWallet, destinationWallet, amount);
        if (transferResponse.getStatus() == HttpStatus.OK) {
            createTransaction(sourceProfile, destinationProfile, amount, transferMoneyRequest);
        }
        return transferResponse;
    }

    private void createTransaction(Profile sourceProfile, Profile destinationProfile, double amount, TransferMoneyRequest transferMoneyRequest) {
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .senderName(sourceProfile.getFullName())
                .recipientName(destinationProfile.getFullName())
                .transactionAmount(amount)
                .description(transferMoneyRequest.getDescription())
                .build();
        saveTransaction(transactionRequest,transferMoneyRequest);
    }


    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private Wallet getWalletByEmailAndId(String email, Long walletId) {
        List<Wallet> wallets = walletRepository.findWalletByEmail(email);
        Optional<Wallet> walletOptional = wallets.stream().filter(wallet -> wallet.getId().equals(walletId)).findFirst();
        return walletOptional.orElse(null);
    }

    private CommonResponse processTransfer(Wallet sourceWallet, Wallet destinationWallet, double amount) {
        if (sourceWallet.getAmount() >= amount) {
            updateWalletAmounts(sourceWallet, destinationWallet, amount);
            return buildResponse(null, WalletConstant.SUCCESSFUL_MONEY_TRANSFER, HttpStatus.OK);
        } else {
            return buildResponse(null, WalletConstant.INSUFFICIENT_ACCOUNT_BALANCE, HttpStatus.BAD_REQUEST);
        }
    }

    private void updateWalletAmounts(Wallet sourceWallet, Wallet destinationWallet, double amount) {
        sourceWallet.setAmount(sourceWallet.getAmount() - amount);
        destinationWallet.setAmount(destinationWallet.getAmount() + amount);
        walletRepository.save(sourceWallet);
        walletRepository.save(destinationWallet);
    }

    private CommonResponse buildResponse(Object data, String message, HttpStatus status) {
        return CommonResponse.builder()
                .data(data)
                .message(message)
                .status(status)
                .build();
    }
    private Wallet getSourceWallet(TransferMoneyRequest request) {
        String sourceEmail = getAuthenticatedEmail();
        return getWalletByEmailAndId(sourceEmail, request.getSourceWalletId());
    }
    private Wallet getDestinationWallet(TransferMoneyRequest request) {
        return getWalletByEmailAndId(request.getDestinationEmail(), request.getDestinationWalletId());
    }
}
