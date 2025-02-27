package com.codegym.finwallet.service.impl;

import com.codegym.finwallet.constant.MailConstant;
import com.codegym.finwallet.constant.TransactionCategoryConstant;
import com.codegym.finwallet.constant.TransactionConstant;
import com.codegym.finwallet.constant.WalletConstant;
import com.codegym.finwallet.converter.TransactionConvert;
import com.codegym.finwallet.converter.TransactionSummaryConvert;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.request.TransactionRequest;
import com.codegym.finwallet.dto.payload.request.TransferMoneyRequest;
import com.codegym.finwallet.dto.payload.response.DateResponse;
import com.codegym.finwallet.dto.payload.response.TransactionResponse;
import com.codegym.finwallet.dto.payload.response.TransactionSummaryResponse;
import com.codegym.finwallet.entity.AppUser;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.entity.Transaction;
import com.codegym.finwallet.entity.TransactionCategory;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.AppUserRepository;
import com.codegym.finwallet.repository.AppUserSettingRepository;
import com.codegym.finwallet.repository.ProfileRepository;
import com.codegym.finwallet.repository.TransactionCategoryRepository;
import com.codegym.finwallet.repository.TransactionRepository;
import com.codegym.finwallet.repository.WalletRepository;
import com.codegym.finwallet.service.TransactionService;
import com.codegym.finwallet.util.AuthUserExtractor;
import com.codegym.finwallet.util.BuildCommonResponse;
import com.codegym.finwallet.util.CreateExcelFile;
import com.codegym.finwallet.util.EmailContentGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final TransactionSummaryConvert convert;
    private final AppUserSettingRepository appUserSettingRepository;
    private final JavaMailSender mailSender;
    private final EmailContentGenerator mailContentGenerator;
    private final CreateExcelFile createExcelFile;
    private final TransactionConvert transactionConvert;
    private final SpringTemplateEngine templateEngine;


    @Override
    @Transactional
    public CommonResponse saveTransaction(TransactionRequest request, Long walletId) {
        String email = userExtractor.getUsernameFromAuth();
        TransactionCategory transactionCategory = getTransactionCategory(request.getTransactionCategoryId());
        boolean isExpense = isTransactionCateGoryExpense(transactionCategory);
        Optional<AppUser> appUserOptional = getUser(email);
        Optional<Wallet> walletOptional = getWallet(walletId);
        if (appUserOptional.isPresent() && walletOptional.isPresent()) {
            Transaction transaction = buildTransaction(request, appUserOptional.get(), walletOptional.get(),transactionCategory,isExpense);
            TransactionResponse transactionResponse = buildResponse(transaction,email);
            transactionRepository.save(transaction);

            Double totalSpent = transactionRepository.getTotalSpendForCategory(transactionCategory.getId());
            if (Math.abs(totalSpent) >= transactionCategory.getBudget()) {
                try {
                    Context emailContext = new Context();
                    emailContext.setVariable("categoryName", transactionCategory.getName());
                    emailContext.setVariable("budgetLimit", Math.abs(transactionCategory.getBudget() + totalSpent));
                    emailContext.setVariable("totalSpent", Math.abs(totalSpent));
                    sendBudgetAlertEmails(email, emailContext);
                } catch (MessagingException e) {
                    e.getMessage();
                }
            }
            return commonResponse.builResponse(transactionResponse, TransactionConstant.CREATE_TRANSACTION_SUCCESSFUL, HttpStatus.CREATED);
        }
        return commonResponse.builResponse(null, TransactionConstant.CREATE_TRANSACTION_FAILED, HttpStatus.BAD_REQUEST);
    }

    @Override
    public void sendBudgetAlertEmails(String email, Context context) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(TransactionCategoryConstant.OVERSPENDING_WARNINGS);
        String emailContext = templateEngine.process("budget-warning-email-template", context);
        mimeMessageHelper.setText(emailContext, true);
        mailSender.send(mimeMessage);
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
        Optional<Transaction> transactionOptional = getTransaction(transactionId);
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
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
            Optional<AppUser> appUserOptional = getUser(email);
            if (appUserOptional.isPresent()) {
                AppUser appUser = appUserOptional.get();
                Transaction senderTransaction = buildTransactionForDeductWallet(request,appUser,senderWallet);
                Transaction receiverTransaction = buildTransactionForReceiverWallet(request,appUser,receiverWallet);

                TransactionResponse transactionResponse = modelMapper.map(senderTransaction,TransactionResponse.class);
                transactionResponse.setFullName(getProfile(email).getFullName());
                return commonResponse.builResponse(transactionResponse, WalletConstant.TRANSFER_MONEY_SUCCESS,HttpStatus.CREATED);
            }
        }catch (Exception e){
            return commonResponse.builResponse(null,WalletConstant.TRANSFER_MONEY_FAILED, HttpStatus.BAD_REQUEST);
        }
        return commonResponse.builResponse(null,WalletConstant.TRANSFER_MONEY_FAILED, HttpStatus.BAD_REQUEST);

    }

    @Override
    public CommonResponse editTransaction(TransactionRequest request, Long walletId, Long transactionId) {
        if (isTransactionInWallet(walletId, transactionId)) {
            Optional<Transaction> transactionOptional = getTransaction(transactionId);
            if (transactionOptional.isPresent() && isRequestAmountAvailable(request,transactionOptional.get())) {
                Transaction transaction = transactionOptional.get();
                updateAmountWalletTypeExpense(request,transaction);
                transaction = transactionConvert.convertRequestToTransaction(request,transaction);
                transactionRepository.save(transaction);
                TransactionResponse transactionResponse = modelMapper.map(transaction,TransactionResponse.class);
                return commonResponse.builResponse(transactionResponse,TransactionConstant.EDIT_TRANSACTION_SUCCESS,HttpStatus.OK);
            }
        }
        return commonResponse.builResponse(null,TransactionConstant.FIND_TRANSACTION_FAILED,HttpStatus.BAD_REQUEST);
    }

    @Override
    public CommonResponse getAllTransactionsAndAmount(Long categoryId,Long walletId) {
        List<Object[]> transactions = transactionRepository.getTotalTransactionAndAmountByTransactionCategory(categoryId,walletId);
        TransactionSummaryResponse transactionSummaryResponse = convert.convertToResponse(transactions);
        return commonResponse.builResponse(transactionSummaryResponse,TransactionConstant.FIND_TRANSACTION_SUCCESSFUL,HttpStatus.OK);
    }


    private TransactionCategory getTransactionCategory(Long id) {
        Optional<TransactionCategory> transactionCategory = transactionCategoryRepository.findById(id);
        return transactionCategory.orElse(null);
    }

    private Optional<AppUser> getUser(String email){
        return appUserRepository.findAppUserByEmail(email);
    }

    private Optional<Wallet> getWallet(Long walletId){
        return walletRepository.findById(walletId);
    }

    private Transaction buildTransaction(TransactionRequest request,AppUser appUser, Wallet wallet,
                                         TransactionCategory transactionCategory, boolean isExpense){
        Transaction transaction = new Transaction();
        if (isTransactionCateGoryExpense(transactionCategory)){
            transaction.setAmount(-request.getAmount());
        }else {
            transaction.setAmount(request.getAmount());
        }
        if (checkSufficientFunds(request,wallet)){
            double walletAmount = wallet.getAmount();
            double requestAmount = request.getAmount();
            double newAmount = walletAmount + requestAmount;
            wallet.setAmount(newAmount);
            transaction.setDescription(request.getDescription());
            transaction.setTransactionDate(request.getTransactionDate());
            transaction.setTransactionCategory(transactionCategory);
            transaction.setAppUser(appUser);
            transaction.setWallet(wallet);
            transaction.setExpense(isExpense);
            transaction.setCurrency(request.getCurrency());
            transactionRepository.save(transaction);
            return transaction;
        }else {
            return null;
        }

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

    private Optional<Transaction> getTransaction(Long transactionId){
        return transactionRepository.findById(transactionId);
    }

    private Wallet plusMoney(TransferMoneyRequest request){
        Optional<Wallet> walletOptional = getWallet(request.getDestinationWalletId());
        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            double currentSpentAmount = wallet.getSpentAmount();
            double newSpentAmount = currentSpentAmount + request.getAmount();
            double inputAmount = request.getAmount();
            double currentAmount = wallet.getAmount();
            double newAmount = currentAmount + inputAmount;
            wallet.setAmount(newAmount);
            wallet.setSpentAmount(newSpentAmount);
            walletRepository.save(wallet);
            return wallet;
        }
        return null;
    }

    private Wallet deductMoney(Long walletId, TransferMoneyRequest request){
        Optional<Wallet> walletOptional = getWallet(walletId);
        if (walletOptional.isPresent()) {
            Wallet wallet = walletOptional.get();
            double currentSpentAmount = wallet.getSpentAmount();
            double newSpentAmount = currentSpentAmount - request.getAmount();
            double currentAmount = wallet.getAmount();
            double newAmount = currentAmount - request.getAmount();
            wallet.setAmount(newAmount);
            wallet.setSpentAmount(newSpentAmount);
            walletRepository.save(wallet);
            return wallet;
        }
        return null;
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
        return transactionCategory.getType().equals("EXPENSE");
    }

    private boolean isTransactionInWallet(Long transactionId, Long walletId){
        Optional<Wallet> wallet = getWallet(walletId);
        Optional<Transaction> transaction = getTransaction(transactionId);
        return transaction.isPresent() && wallet.isPresent();
    }

    private double minusMoney(TransactionRequest request, Wallet wallet){
        double currentMoney = wallet.getAmount();
        double newAmount = currentMoney - request.getAmount();
        wallet.setSpentAmount(request.getAmount());
        wallet.setAmount(newAmount);

        walletRepository.save(wallet);
        return newAmount;
    }

    private DateResponse getDateInWeek(){
        return DateResponse.builder()
                .startDate(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))
                .endDate(LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)))
                .build();
    }

    private DateResponse getDateInMonth(){
        return DateResponse.builder()
                .startDate(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()))
                .endDate(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))
                .build();
    }

    private boolean checkSufficientFunds(TransactionRequest request, Wallet wallet){
        double currentAmount = wallet.getAmount();
        double inputAmount = request.getAmount();
        return currentAmount >= inputAmount;
    }

    private List<Transaction> getTransactionsForToday(Long walletId){
        LocalDate today = LocalDate.now();
        return transactionRepository.findTransactionForTime(today,today,walletId);
    }

    private List<Transaction> getTransactionsForWeek(Long walletId){
        DateResponse dateResponse = getDateInWeek();
        return transactionRepository.findTransactionForTime(dateResponse.getStartDate(),dateResponse.getEndDate(),walletId);
    }

    private List<Transaction> getTransactionsForMonth(Long walletId){
        DateResponse dateResponse = getDateInMonth();
        return transactionRepository.findTransactionForTime(dateResponse.getStartDate(),dateResponse.getEndDate(),walletId);
    }

    @Scheduled(cron = "0 55 23 L * ?")
    public void sendScheduledEmailsForMonth(){
        List<String> emails = appUserSettingRepository.getListEmailTypeWeek();
        LocalDate startWeekDay = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endWeekDay = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        DateResponse response = new DateResponse(startWeekDay, endWeekDay);
        sendScheduledEmails(emails,MailConstant.MONTH_SUBJECT,response);
    }

    @Scheduled(cron = "0 55 23 ? * SAT")
    public void sendScheduledEmailsForWeek(){
        List<String> emails = appUserSettingRepository.getListEmailTypeWeek();
        LocalDate startWeekDay = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endWeekDay = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        DateResponse response = new DateResponse(startWeekDay, endWeekDay);
        sendScheduledEmails(emails,MailConstant.WEEK_SUBJECT,response);
    }

//    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "0 55 23 * * ?")
    public void sendScheduledEmailsForDay(){
        List<String> emails = appUserSettingRepository.getListEmailTypeDay();
        DateResponse dateResponse = new DateResponse(LocalDate.now(),LocalDate.now());
        sendScheduledEmails(emails,MailConstant.DAY_SUBJECT,dateResponse);
    }

    private List<Wallet> getAllWalletByEmail(String email){
        return walletRepository.findWalletByEmail(email);
    }

    private void sendScheduledEmails(List<String> emailList, String subject, DateResponse response) {
        LocalDate startDate = response.getStartDate();
        LocalDate endDate = response.getEndDate();
        List<Profile> profiles = new ArrayList<>();
        for (String email : emailList) {
            try {
                List<Wallet> wallets = getAllWalletByEmail(email);
                Profile profile = getProfileByEmail(email);
                profiles.add(profile);
                if (wallets.isEmpty()) {
                    continue;
                }

                Map<Wallet, List<Transaction>> walletTransactionsMap = new HashMap<>();
                for (Wallet wallet : wallets) {
                    List<Transaction> transactions = transactionRepository.findTransactionForTime(startDate,endDate,wallet.getId());
                    walletTransactionsMap.put(wallet, transactions);
                }

                String excelFilePath = createExcelFile.createExcelFile(walletTransactionsMap);

                String emailContent = mailContentGenerator.generateEmailContent(email, wallets,excelFilePath);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
                messageHelper.setTo(email);
                messageHelper.setSubject(subject);
                messageHelper.setText(emailContent, true);

                mailSender.send(message);

            } catch (MessagingException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Profile getProfileByEmail(String email){
        return profileRepository.findProfileByEmail(email).orElse(null);
    }


    private void updateAmountWalletTypeExpense(TransactionRequest request, Transaction transaction){
        Wallet wallet = transaction.getWallet();
        double requestAmount = request.getAmount();
        double transactionAmount = transaction.getAmount();
        double currentAmount = wallet.getAmount();
        double newAmount = 0;
        double newSpentAmount = 0;
        double spentAmount = wallet.getSpentAmount();
        if (requestAmount > transactionAmount) {
            newAmount = requestAmount - transactionAmount;
            currentAmount -= newAmount;
            newSpentAmount = spentAmount + newAmount;
        }else {
            newAmount = transactionAmount - requestAmount;
            currentAmount += newAmount;
            newSpentAmount = spentAmount - newAmount;
        }
        wallet.setAmount(currentAmount);
        wallet.setSpentAmount(newSpentAmount);
        walletRepository.save(wallet);
    }

    private boolean isRequestAmountAvailable(TransactionRequest request, Transaction transaction){
        Wallet wallet = transaction.getWallet();
        double walletAmount = wallet.getAmount();
        double transactionAmount = transaction.getAmount();
        double requestAmount = request.getAmount();
        double totalAmount = walletAmount + transactionAmount;
        return requestAmount <= totalAmount;
    }
}
