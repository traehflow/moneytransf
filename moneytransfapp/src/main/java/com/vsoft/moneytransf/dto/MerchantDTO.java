package com.vsoft.moneytransf.dto;

import com.vsoft.moneytransf.MerchantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Currency;

@Data
public class MerchantDTO {
    private Long id;
    private String name;
    private String description;
    private String email;
    private MerchantStatus status;
    private Currency totalTransactionSum;
}