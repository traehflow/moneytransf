package com.vsoft.moneytransf.dto;

import com.vsoft.moneytransf.jpl.entity.Transaction;
import com.vsoft.moneytransf.jpl.entity.TransactionDescriminator;
import com.vsoft.moneytransf.validation.Email;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class InputTransactionDTO {
    TransactionDescriminator transactionType;
    BigDecimal amount;
    @NotBlank(message = "email required.")
    @Email
    String customerEmail;
    String customerPhone;
    UUID referencedTransactionId;
}
