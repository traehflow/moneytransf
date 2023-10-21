package com.vsoft.moneytransf.jpl.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CHARGE")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChargeTransaction extends Transaction {
    BigDecimal refundedAmount;
}
