package com.vsoft.moneytransf.jpl.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("REVERSAL")
@Data
@EqualsAndHashCode(callSuper = true)
public class ReversalTransaction extends Transaction{
}
