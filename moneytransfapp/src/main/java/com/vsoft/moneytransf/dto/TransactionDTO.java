package com.vsoft.moneytransf.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionDTO {
    UUID transactionId;
    String customerPhone;
    String customerEmail;
    BigDecimal amount;
}
