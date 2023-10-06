package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.CleanupTasks;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.jpl.entity.AuthorizeTransaction;
import com.vsoft.moneytransf.jpl.entity.ChargeTransaction;
import com.vsoft.moneytransf.jpl.entity.Merchant;
import com.vsoft.moneytransf.jpl.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController("api/generals")
public class MerchantController {
    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CleanupTasks cleanupTasks;


    @GetMapping("/save")
    public void save() {
        Merchant merchant = new Merchant();
        merchant.setName("name");
        merchant.setDescription("This is a merchant " + System.currentTimeMillis());
        merchantRepository.save(merchant);
        ChargeTransaction cTransaction = new ChargeTransaction();
        cTransaction.setAmount(new BigDecimal("1222.12"));
        transactionRepository.save(cTransaction);
        AuthorizeTransaction aTransaction = new AuthorizeTransaction();
        aTransaction.setAmount(new BigDecimal("2343.23"));
        transactionRepository.save(aTransaction);
    }

    @GetMapping("/list")
    public List<String> list() {
        return merchantRepository.list();
    }
    @GetMapping("/leading")
    public String leading(){
        return "some content";
    }

    @PostMapping("/forceCleanup")
    public void forceCleanup() {
        cleanupTasks.cleanupTransactions();
    }

    @PostMapping("/forceClean")
    public void forceClean(long millisAgo) {
        transactionRepository.deleteByTimestampBefore(System.currentTimeMillis() - millisAgo);
    }
}
