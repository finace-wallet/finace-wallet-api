package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.WalletConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.entity.Transaction;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.TransactionRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.ProfileService;
import com.codegym.finwallet.repository.WalletTransactionRepository;
import com.codegym.finwallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ProfileService profileService;
    private final WalletRepository walletRepository;


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

    @Override
    public CommonResponse transferMoney(TransferMoneyRequest transferMoneyRequest) {
        String sourceEmail = getAuthenticatedEmail();
        Profile sourceProfile = profileService.findProfileByEmail(sourceEmail);
        Profile destinationProfile = profileService.findProfileByEmail(transferMoneyRequest.getDestinationEmail());

        if (sourceProfile == null || destinationProfile == null) {
            return buildResponse(null, WalletConstant.PROFILE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        Wallet sourceWallet = getWalletByEmailAndId(sourceEmail, transferMoneyRequest.getSourceWalletId());
        Wallet destinationWallet = getWalletByEmailAndId(transferMoneyRequest.getDestinationEmail(), transferMoneyRequest.getDestinationWalletId());

        if (sourceWallet == null || destinationWallet == null) {
            return buildResponse(null, WalletConstant.WALLET_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
        }

        double amount = transferMoneyRequest.getAmount();
        return processTransferAndUpdateTransaction(sourceProfile, destinationProfile, sourceWallet, destinationWallet, amount, transferMoneyRequest);
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
        create(transactionRequest);
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
}