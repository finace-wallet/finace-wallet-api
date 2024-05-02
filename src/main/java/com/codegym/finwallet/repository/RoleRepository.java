package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleType(String roleType);
    boolean existsByRoleType(String roleType);
    @Query("SELECT r FROM AppUser u JOIN u.roles r WHERE u.email = :email")
    List<Role> findRolesByEmail(String email);
}
