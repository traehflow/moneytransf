package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.dto.MerchantDTO;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.service.impl.MerchantsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("merchants")
public class MerchantController {
    @Autowired
    MerchantsServiceImpl merchantsService;

    @GetMapping()
    public List<Merchant> list() {
        return merchantsService.listAll();
    }

    @PutMapping("/{merchantId}")
    public Merchant update(UUID merchantId, @Valid @RequestBody MerchantDTO merchantDTO) {
        return merchantsService.update(merchantId, merchantDTO);
    }
}
