package com.vsoft.moneytransf.dto;

import lombok.Data;
import lombok.Generated;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Generated
public class OutputTransactionDTO {
    UUID transactionId;
    UUID referencedTransactionId;
    BigDecimal amount;
}
