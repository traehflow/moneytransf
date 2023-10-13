package com.vsoft.moneytransf.dto;

import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.validation.Email;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Data
public class MerchantDTO {
    private Long id;
    private String name;
    private String description;
    @Email
    private String email;
    private MerchantStatus status;

    private BigDecimal totalTransactionSum;
}
