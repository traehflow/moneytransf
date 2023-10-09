package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.CleanupTasks;
import com.vsoft.moneytransf.Roles;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.service.MerchantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("api/generals")
public class MerchantController {
    @Autowired
    MerchantsService merchantsService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CleanupTasks cleanupTasks;

    @GetMapping("/list")
    public List<Merchant> list() {
        return merchantsService.listAll();
    }

    @Secured(Roles.ROLE_PREFIX + Roles.ADMIN)
    @PostMapping("/forceCleanup")
    public void forceCleanup() {
        cleanupTasks.cleanupTransactions();
    }

    @PostMapping("/forceClean")
    public void forceClean(long millisAgo) {
        transactionRepository.deleteByTimestampBefore(System.currentTimeMillis() - millisAgo);
    }
}
