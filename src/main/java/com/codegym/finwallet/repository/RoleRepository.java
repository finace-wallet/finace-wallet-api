package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleType(String roleType);
    boolean existsByRoleType(String roleType);
}
