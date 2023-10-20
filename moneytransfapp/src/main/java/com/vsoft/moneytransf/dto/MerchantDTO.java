package com.vsoft.moneytransf.dto;

import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.validation.Email;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MerchantDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Email
    private String email;
    private MerchantStatus status;
}
