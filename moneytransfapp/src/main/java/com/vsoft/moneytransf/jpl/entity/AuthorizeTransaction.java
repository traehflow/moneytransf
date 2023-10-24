package com.vsoft.moneytransf.jpl.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("AUTHORIZE")
@Data
@Generated
@EqualsAndHashCode(callSuper = true)
public class AuthorizeTransaction extends Transaction{
    @DecimalMin(value = "0", inclusive = false, message = "Amount must be greater than 0")
    BigDecimal amount;
}
