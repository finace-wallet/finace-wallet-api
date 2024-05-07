package com.codegym.finwallet.service;

import jakarta.annotation.PostConstruct;

public interface RoleInitializationService {
    @PostConstruct
    void init();
}
