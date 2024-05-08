package com.codegym.finwallet.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUserExtractor {
    public String getUsernameFromAuth(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
