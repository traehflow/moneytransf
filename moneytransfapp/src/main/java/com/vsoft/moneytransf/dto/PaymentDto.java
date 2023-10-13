package com.vsoft.moneytransf.dto;

import com.vsoft.moneytransf.validation.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    @NotBlank(message = "email required.")
    @Email
    String merchantEmail;
    BigDecimal amount;
    @NotBlank(message = "email required.")
    @Email
    String customerEmail;
    String customerPhone;
}

