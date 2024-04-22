package com.codegym.finwallet.repository;

import com.codegym.finwallet.constant.HqlConstant;
import com.codegym.finwallet.entity.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {
    @Query(value = "SELECT tbl " +
            "FROM TokenBlackList tbl  " +
            "JOIN tbl.user au " +
            "WHERE au.email = :email")
    TokenBlackList findByEmail(String email);

}
