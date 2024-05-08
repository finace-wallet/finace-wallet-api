package com.codegym.finwallet.repository;

import com.codegym.finwallet.entity.OwnerShip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerShipRepository extends JpaRepository<OwnerShip, Long> {
    Optional<OwnerShip> findByName(String name);
}
