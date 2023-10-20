package com.vsoft.moneytransf.dto;

import com.vsoft.moneytransf.jpl.entity.Transaction;
import com.vsoft.moneytransf.jpl.entity.TransactionDescriminator;
import com.vsoft.moneytransf.validation.Email;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class InputTransactionDTO {
    TransactionDescriminator transactionType;
    @Min(value =  0, message = "Amount must be positive.")
    BigDecimal amount;
    @NotBlank(message = "email required.")
    @Email
    String customerEmail;
    @NotBlank
    String customerPhone;
    UUID referencedTransactionId;
}
