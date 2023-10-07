package com.vsoft.moneytransf.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentDto {
    String merchantEmali;
    BigDecimal amount;
    String customerEmail;
    String customerPhone;
}

