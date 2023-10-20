package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.Roles;
import com.vsoft.moneytransf.UserProfile;
import com.vsoft.moneytransf.dto.*;
import com.vsoft.moneytransf.service.TransactionsService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.CleanupTasks;


@RestController
@RequestMapping("/transactions/")
public class TransactionController {

    private final TransactionsService transactionsService;

    private final TransactionRepository transactionRepository;

    private final CleanupTasks cleanupTasks;

    private final UserProfile userProfile;

    public TransactionController(TransactionsService transactionsService, TransactionRepository transactionRepository,
                                 CleanupTasks cleanupTasks, UserProfile userProfile) {
        this.transactionsService = transactionsService;

        //I will remove it from here
        this.transactionRepository = transactionRepository;
        this.cleanupTasks = cleanupTasks;
        this.userProfile = userProfile;
    }

    @Secured(Roles.ROLE_PREFIX + Roles.MERCHANT)
    @PostMapping()
    public OutputTransactionDTO input(@Valid @RequestBody InputTransactionDTO inputTransactionDTO) {
        return transactionsService.executeTransaction(inputTransactionDTO, userProfile.getUserName());
    }

    @Secured(Roles.ROLE_PREFIX + Roles.ADMIN)
    @DeleteMapping()
    public void forceCleanup() {
        cleanupTasks.cleanupTransactions();
    }

    @PostMapping("/forceClean")
    public void forceClean(long millisAgo) {
        transactionRepository.deleteByTimestampBefore(System.currentTimeMillis() - millisAgo);
    }
}
