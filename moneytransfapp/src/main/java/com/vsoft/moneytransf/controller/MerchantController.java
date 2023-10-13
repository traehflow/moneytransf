package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.Roles;
import com.vsoft.moneytransf.dto.MerchantDTO;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.service.MerchantsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("merchants")
public class MerchantController {
    @Autowired
    MerchantsService merchantsService;

    @GetMapping("/list")
    public List<Merchant> list() {
        return merchantsService.listAll();
    }

    @PutMapping("/update/{merchantId}")
    public Merchant update(UUID merchantId, @Valid @RequestBody MerchantDTO merchantDTO) {
        return merchantsService.update(merchantId, merchantDTO);
    }

}
