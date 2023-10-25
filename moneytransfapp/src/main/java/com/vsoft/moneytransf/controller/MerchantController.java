package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.Roles;
import com.vsoft.moneytransf.UserProfile;
import com.vsoft.moneytransf.dto.MerchantDTO;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.service.MerchantsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/merchants/")
public class MerchantController {

    private final MerchantsService merchantsService;

    private final UserProfile userProfile;


    public MerchantController(MerchantsService merchantsService, UserProfile userProfile) {
        this.merchantsService = merchantsService;
        this.userProfile = userProfile;
    }

    @GetMapping()
    public List<Merchant> list() {
        if(userProfile.isMerchant()) {
            return List.of(merchantsService.getByEmail(userProfile.getUserName()));
        }
        if(userProfile.isAdmin()) {
            return merchantsService.listAll();
        }
        throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
    }

    @Secured(Roles.ROLE_PREFIX + Roles.ADMIN)
    @PutMapping("/{merchantId}")
    public Merchant update(UUID merchantId, @Valid @RequestBody MerchantDTO merchantDTO) {
        return merchantsService.update(merchantId, merchantDTO);
    }

    @Secured(Roles.ROLE_PREFIX + Roles.MERCHANT)
    @PutMapping("/")
    public Merchant updateMyself(@Valid @RequestBody MerchantDTO merchantDTO) {
        return merchantsService.updateByEmail(userProfile.getUserName(), merchantDTO);
    }

}
