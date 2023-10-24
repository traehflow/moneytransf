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

    public TransactionDTO(long timestamp, UUID id, TransactionStatus status, UUID referencedTransactionId, BigDecimal amount) {
        this.timestamp = timestamp;
        this.id = id;
        this.status = status;
        this.referencedTransactionId = referencedTransactionId;
        this.amount = amount;
    }

    public TransactionDTO(long timestamp, UUID id, TransactionStatus status, UUID referencedTransactionId) {
        this.timestamp = timestamp;
        this.id = id;
        this.status = status;
        this.referencedTransactionId = referencedTransactionId;
    }

    public TransactionDTO(long timestamp, UUID id, TransactionStatus status, BigDecimal amount) {
        this.timestamp = timestamp;
        this.id = id;
        this.status = status;
        this.amount = amount;
    }
}
