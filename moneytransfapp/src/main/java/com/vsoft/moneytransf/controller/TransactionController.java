package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.Roles;
import com.vsoft.moneytransf.dto.PaymentDto;
import com.vsoft.moneytransf.dto.PaymentResultDTO;
import com.vsoft.moneytransf.dto.ReversePaymentDTO;
import com.vsoft.moneytransf.dto.TransactionDTO;
import com.vsoft.moneytransf.service.TransactionsService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("transactions")
public class TransactionController {

    private final TransactionsService transactionsService;

    public TransactionController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @Secured(Roles.ROLE_PREFIX + Roles.MERCHANT)
    @PostMapping("/pay")
    public PaymentResultDTO pay(@Valid @RequestBody PaymentDto paymentDto) {
        return transactionsService.executePayment(paymentDto);
    }

    @Secured(Roles.ROLE_PREFIX + Roles.MERCHANT)
    @PostMapping("/hold")
    public PaymentResultDTO hold(@Valid PaymentDto paymentDto) {
        return transactionsService.hold(paymentDto);
    }

    @Secured(Roles.ROLE_PREFIX + Roles.MERCHANT)
    @PostMapping("/reverse")
    public TransactionDTO reverse(@Valid ReversePaymentDTO reversePaymentDTO) {
        return transactionsService.reversePayment(reversePaymentDTO);
    }
}
