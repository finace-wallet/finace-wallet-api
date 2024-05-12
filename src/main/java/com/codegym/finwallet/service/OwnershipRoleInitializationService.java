package com.codegym.finwallet.service;

import jakarta.annotation.PostConstruct;

public interface OwnershipRoleInitializationService {
    @PostConstruct
    void init();
}
