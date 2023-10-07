package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.dto.PaymentDto;
import com.vsoft.moneytransf.dto.PaymentResultDTO;
import com.vsoft.moneytransf.service.TransactionsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("transactions")
public class TransactionController {

    private final TransactionsService transactionsService;

    public TransactionController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @PostMapping("/pay")
    public PaymentResultDTO pay(PaymentDto paymentDto) {
        return transactionsService.executePayment(paymentDto);
    }

    @PostMapping("/hold")
    public PaymentResultDTO hold(PaymentDto paymentDto) {
        return transactionsService.hold(paymentDto);
    }

}
