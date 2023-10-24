package com.vsoft.moneytransf.dto;

import com.vsoft.moneytransf.MerchantStatus;
import com.vsoft.moneytransf.validation.Email;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Generated
public class MerchantDTO {
    private String name;
    private String description;
    @Email
    private String email;
    private MerchantStatus status;
}
