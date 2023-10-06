package com.vsoft.moneytransf.jpl.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@DiscriminatorValue("REFUND")
@Data
@EqualsAndHashCode(callSuper = true)
public class RefundTransaction extends Transaction {
    UUID chargeTransactionId;
}
