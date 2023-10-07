package com.vsoft.moneytransf.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentResultDTO {
    UUID authorizeTransactionId;
    UUID chargeTransactionId;
    BigDecimal amount;
}
