package com.vsoft.moneytransf.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReversePaymentDTO {
    UUID transactionId;
}
