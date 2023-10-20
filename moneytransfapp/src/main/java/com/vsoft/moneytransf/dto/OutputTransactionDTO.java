package com.vsoft.moneytransf.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OutputTransactionDTO {
    UUID transactionId;
    UUID referencedTransactionId;
    BigDecimal amount;
}
