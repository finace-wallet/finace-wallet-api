package com.codegym.finwallet.service;

import jakarta.annotation.PostConstruct;

public interface TransactionCategoryDefaultInitializationService {
    @PostConstruct
    void init();
}
