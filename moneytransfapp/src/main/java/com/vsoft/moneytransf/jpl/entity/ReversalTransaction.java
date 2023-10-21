package com.vsoft.moneytransf.jpl.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("REVERSAL")
@Data
@EqualsAndHashCode(callSuper = true)
public class ReversalTransaction extends Transaction{
    @ManyToOne
    @JoinColumn(name = "reference_id", referencedColumnName = "id")
    Transaction referencedTransaction;
}
