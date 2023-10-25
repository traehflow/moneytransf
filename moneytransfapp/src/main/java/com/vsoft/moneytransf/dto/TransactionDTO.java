package com.vsoft.moneytransf.dto;

import com.vsoft.moneytransf.TransactionStatus;
import lombok.Data;
import lombok.Generated;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Generated
public class TransactionDTO {
    UUID id;
    String customerEmail;
    UUID referencedTransactionId;
    BigDecimal amount;
    TransactionStatus status;
    long timestamp;

    public TransactionDTO(long timestamp, UUID id, TransactionStatus status, String customerEmail, UUID referencedTransactionId, BigDecimal amount) {
        this.timestamp = timestamp;
        this.id = id;
        this.status = status;
        this.customerEmail = customerEmail;
        this.referencedTransactionId = referencedTransactionId;
        this.amount = amount;
    }

    public TransactionDTO(long timestamp, UUID id, TransactionStatus status, String customerEmail, UUID referencedTransactionId) {
        this.timestamp = timestamp;
        this.id = id;
        this.status = status;
        this.customerEmail = customerEmail;
        this.referencedTransactionId = referencedTransactionId;
    }

    public TransactionDTO(long timestamp, UUID id, TransactionStatus status, String customerEmail, BigDecimal amount) {
        this.timestamp = timestamp;
        this.id = id;
        this.customerEmail = customerEmail;
        this.status = status;
        this.amount = amount;
    }
}
