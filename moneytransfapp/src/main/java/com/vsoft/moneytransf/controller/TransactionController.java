package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.Roles;
import com.vsoft.moneytransf.dto.*;
import com.vsoft.moneytransf.service.TransactionsService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.vsoft.moneytransf.jpl.TransactionRepository;
import com.vsoft.moneytransf.CleanupTasks;


@RestController("transactions")
public class TransactionController {

    private final TransactionsService transactionsService;

    private final TransactionRepository transactionRepository;

    private final CleanupTasks cleanupTasks;

    public TransactionController(TransactionsService transactionsService, TransactionRepository transactionRepository,
                                 CleanupTasks cleanupTasks) {
        this.transactionsService = transactionsService;

        //I will remove it from here
        this.transactionRepository = transactionRepository;
        this.cleanupTasks = cleanupTasks;
    }

    @Secured(Roles.ROLE_PREFIX + Roles.MERCHANT)
    @PostMapping("/pay")
    public PaymentResultDTO pay(@Valid @RequestBody PaymentDto paymentDto) {
        return transactionsService.executePayment(paymentDto);
    }

    @Secured(Roles.ROLE_PREFIX + Roles.MERCHANT)
    @PostMapping("/hold")
    public PaymentResultDTO hold(@Valid @RequestBody PaymentDto paymentDto) {
        return transactionsService.hold(paymentDto);
    }

    @Secured(Roles.ROLE_PREFIX + Roles.MERCHANT)
    @PostMapping("/reverse")
    public TransactionDTO reverse(@Valid @RequestBody ReversePaymentDTO reversePaymentDTO) {
        return transactionsService.reversePayment(reversePaymentDTO);
    }

    @Secured(Roles.ROLE_PREFIX + Roles.MERCHANT)
    @PostMapping("/refund")
    public TransactionDTO refund(@Valid @RequestBody RefundPaymentDTO refundPaymentDTO) {
        return transactionsService.refundPayment(refundPaymentDTO);
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
