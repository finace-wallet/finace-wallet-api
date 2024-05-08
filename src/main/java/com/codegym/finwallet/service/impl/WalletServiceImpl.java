package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.WalletConstant;
import com.codegym.finwallet.constant.WalletOwnershipConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;
import com.codegym.finwallet.dto.payload.request.WalletRequest;
import com.codegym.finwallet.dto.payload.response.WalletResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.OwnerShip;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.entity.WalletOwnership;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.OwnerShipRepository;
import com.codegym.finwallet.repository.WalletOwnershipRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.WalletService;
import com.codegym.finwallet.util.AuthUserExtractor;
import com.codegym.finwallet.util.BuildCommonResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final AppUserRepository appUserRepository;
    private final WalletOwnershipRepository walletOwnershipRepository;
    private final BuildCommonResponse commonResponse;
    private final AuthUserExtractor authUserExtractor;
    private final OwnerShipRepository ownerShipRepository;

    @Override
    public Page<Wallet> findAllByEmail(Pageable pageable) {
        String email = authUserExtractor.getUsernameFromAuth();
        return walletRepository.findAllByEmail(pageable, email);
    }

    @Override
    public CommonResponse findWalletByViewer(Pageable pageable) {
        AppUser appUser = findAppUserByEmail();
        Page<Wallet> filteredWallets = walletRepository.findAllByEmailAndViewer(pageable, appUser.getEmail());
        List<WalletResponse> walletResponses = mapToWalletResponse(filteredWallets.getContent());
        PageImpl<WalletResponse> walletResponsePage = new PageImpl<>(walletResponses, pageable, walletResponses.size());
        return commonResponse.builResponse(walletResponsePage,"",HttpStatus.OK);
    }

    @Override
    public CommonResponse findWalletsByEmailAndOwner(Pageable pageable) {
        AppUser appUser = findAppUserByEmail();
        Page<Wallet> filteredWallets = walletRepository.findAllByEmailAndOwner(pageable, appUser.getEmail());
        List<WalletResponse> walletResponses = mapToWalletResponse(filteredWallets.getContent());
        PageImpl<WalletResponse> walletResponsePage = new PageImpl<>(walletResponses, pageable, walletResponses.size());
        return commonResponse.builResponse(walletResponsePage,"",HttpStatus.OK);
    }

    @Override
    public CommonResponse findWalletsByCoOwner(Pageable pageable) {
        AppUser appUser = findAppUserByEmail();
        Page<Wallet> filteredWallets = walletRepository.findAllByCoOwner(pageable, appUser.getEmail());
        List<WalletResponse> walletResponses = mapToWalletResponse(filteredWallets.getContent());
        PageImpl<WalletResponse> walletResponsePage = new PageImpl<>(walletResponses, pageable, walletResponses.size());
        return commonResponse.builResponse(walletResponsePage,"",HttpStatus.OK);
    }

    private AppUser findAppUserByEmail() {
        return appUserRepository.findFirstByEmail(authUserExtractor.getUsernameFromAuth());
    }

    private List<WalletResponse> mapToWalletResponse(List<Wallet> wallets) {
        return wallets.stream()
                .map(this::mapWalletToResponse)
                .toList();
    }

    private WalletResponse mapWalletToResponse(Wallet wallet) {
        WalletResponse walletResponse = modelMapper.map(wallet, WalletResponse.class);
        WalletOwnership walletOwnership = wallet.getWalletOwnerships().getFirst();
        walletResponse.setOwnership(walletOwnership.getOwnerShip().getName());
        return walletResponse;
    }

    @Override
    public Page<Wallet> findAllRecipientByEmail(Pageable pageable, String email) {
        return walletRepository.findAllByEmail(pageable, email);
    }

    @Override
    public CommonResponse createWallet(WalletRequest request) {
        try {
            AppUser appUser = appUserRepository.findByEmail(authUserExtractor.getUsernameFromAuth());
            Wallet wallet = saveWallet(request);
            WalletResponse response = saveWalletOwnerShip(wallet,appUser);
            if (response != null){
                return commonResponse.builResponse(response, WalletConstant.CREATE_NEW_WALLET_SUCCESS_MESSAGE, HttpStatus.CREATED);
            }
            return commonResponse.builResponse(null,WalletConstant.CREATE_NEW_WALLET_FAIL_MESSAGE, HttpStatus.BAD_REQUEST);
        } catch (AuthenticationException e) {
            return commonResponse.builResponse(null, e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    //chua fix
    @Override
    public CommonResponse deleteWallet(Long id) {
        try {
            Optional<Wallet> walletOptional = walletRepository.findById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            if (walletOptional.isPresent() && isUserWallet(id, email)) {
                Wallet wallet = walletOptional.get();
                wallet.setDelete(true);
                walletRepository.save(wallet);
                return commonResponse.builResponse(null, WalletConstant.UPDATE_WALLET_INFORMATION_SUCCESS_MESSAGE, HttpStatus.OK);
            }
        } catch (SecurityException e) {
            return commonResponse.builResponse(null, WalletConstant.UPDATE_WALLET_INFORMATION_FAILURE_MESSAGE, HttpStatus.UNAUTHORIZED);
        }
        return commonResponse.builResponse(null, WalletConstant.UPDATE_WALLET_INFORMATION_DENIED, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public Wallet findById(Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        Wallet newWallet = new Wallet();
        if (wallet.isPresent()) {
            newWallet = wallet.get();
        }
        return newWallet;
    }

    @Override
    public CommonResponse editWallet(WalletRequest walletRequest, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        try {
            Optional<Wallet> walletOptional = walletRepository.findById(id);
            if (!walletOptional.isPresent()) {

                return commonResponse.builResponse(null, WalletConstant.WALLET_NOT_FOUND_MESSAGE, HttpStatus.OK);
            }
            if (isUserWallet(id, email)) {
                Wallet wallet = walletOptional.get();
                double currentAmount = wallet.getAmount();
                double inputAmount = walletRequest.getAmount();
                if (inputAmount < 0) {
                    return commonResponse.builResponse(null, WalletConstant.AMOUNT_NOT_AVAILABLE, HttpStatus.BAD_REQUEST);
                }
                double newAmount = currentAmount + inputAmount;
                wallet.setAmount(newAmount);
                wallet.setName(walletRequest.getName());
                wallet.setDescription(walletRequest.getDescription());
                walletRepository.save(wallet);
                WalletResponse walletResponse = modelMapper.map(wallet, WalletResponse.class);
                return commonResponse.builResponse(walletResponse, WalletConstant.UPDATE_WALLET_INFORMATION_SUCCESS_MESSAGE, HttpStatus.OK);
            } else {
                return commonResponse.builResponse(null, WalletConstant.UPDATE_WALLET_INFORMATION_DENIED, HttpStatus.FORBIDDEN);
            }
        } catch (SecurityException e) {
            return commonResponse.builResponse(null, WalletConstant.UPDATE_WALLET_INFORMATION_FAILURE_MESSAGE, HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isUserWallet(Long walletId, String email) {
        List<Wallet> wallets = walletRepository.findWalletByEmail(email);
        Optional<Wallet> walletOptional = wallets.stream().filter(w -> w.getId().equals(walletId))
                .findFirst();
        return walletOptional.isPresent();
    }


    @Override
    public CommonResponse addMoneyToWallet(Long walletId, double amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        List<Wallet> wallets = walletRepository.findWalletByEmail(userEmail);

        Optional<Wallet> walletOptional = wallets.stream().filter(wallet -> wallet.getId().equals(walletId)).findFirst();
        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            double currentAmount = wallet.getAmount();
            wallet.setAmount(currentAmount + amount);
            walletRepository.save(wallet);
            return commonResponse.builResponse(null, WalletConstant.MONEY_ADDED_SUCCESSFULLY, HttpStatus.OK);
        } else {
            return commonResponse.builResponse(null, WalletConstant.WALLET_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
        }
    }

    private Wallet saveWallet(WalletRequest walletRequest) {
        Wallet wallet = modelMapper.map(walletRequest, Wallet.class);
        walletRepository.save(wallet);
        return wallet;
    }

    private WalletResponse saveWalletOwnerShip(Wallet wallet, AppUser appUser) {
        Optional<OwnerShip> ownerShipOptional = ownerShipRepository.findByName(WalletOwnershipConstant.OWNERSHIP_OWNER);
        if (ownerShipOptional.isPresent()) {
            WalletOwnership ownership = new WalletOwnership();
            ownership.setOwnerShip(ownerShipOptional.get());
            ownership.setWallet(wallet);
            ownership.setAppUser(appUser);
            walletOwnershipRepository.save(ownership);
            WalletResponse response = modelMapper.map(wallet, WalletResponse.class);
            response.setOwnership(ownership.getOwnerShip().getName());
            return response;
        }
        return null;
    }
}
