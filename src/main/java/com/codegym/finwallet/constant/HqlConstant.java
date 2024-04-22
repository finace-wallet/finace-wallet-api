package com.codegym.finwallet.constant;

public class HqlConstant {
    public static final String SELECT_TOKEN_FROM_BLACK_LIST_BY_EMAIL = "SELECT tb.token FROM TokenBlackList tb JOIN AppUser u WHERE u.email = :email";
}
