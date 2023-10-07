package com.vsoft.moneytransf.jpl.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MerchRepostitory extends JpaRepository<Merchant, UUID> {
    List<Merchant> findAll();
}
