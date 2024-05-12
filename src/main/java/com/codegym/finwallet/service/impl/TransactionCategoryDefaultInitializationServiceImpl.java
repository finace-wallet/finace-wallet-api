//package com.codegym.finwallet.service.impl;
//
//import com.codegym.finwallet.constant.TransactionCategoryConstant;
//import com.codegym.finwallet.entity.TransactionCategoryDefault;
//import com.codegym.finwallet.repository.TransactionCategoryDefaultRepository;
//import com.codegym.finwallet.service.TransactionCategoryDefaultInitializationService;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class TransactionCategoryDefaultInitializationServiceImpl implements TransactionCategoryDefaultInitializationService {
//
//    private final TransactionCategoryDefaultRepository transactionCategoryDefaultRepository;
//
//    @PostConstruct
//    public void init() {
//        initializeTransactionCategory();
//    }
//
//    private void initializeTransactionCategory() {
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.FOOD_AND_DRINK)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.FOOD_AND_DRINK);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_EXPENSE);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.SHOPPING)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.SHOPPING);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_EXPENSE);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.TRANSPORT)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.TRANSPORT);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_EXPENSE);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.HOME)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.HOME);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_EXPENSE);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.BILL_AND_FEES)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.BILL_AND_FEES);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_EXPENSE);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.SALARY)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.SALARY);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_INCOME);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.LOAN)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.LOAN);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_INCOME);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.BUSINESS)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.BUSINESS);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_INCOME);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.INSURANCE)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.INSURANCE);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_INCOME);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.OTHER)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.OTHER);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_INCOME);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//        if (!transactionCategoryDefaultRepository.existsByName(TransactionCategoryConstant.ENTERTAINMENT)){
//            TransactionCategoryDefault transactionCategoryDefault = new TransactionCategoryDefault();
//            transactionCategoryDefault.setName(TransactionCategoryConstant.ENTERTAINMENT);
//            transactionCategoryDefault.setType(TransactionCategoryConstant.CATEGORY_TYPE_INCOME);
//            transactionCategoryDefaultRepository.save(transactionCategoryDefault);
//        }
//
//    }
//}
