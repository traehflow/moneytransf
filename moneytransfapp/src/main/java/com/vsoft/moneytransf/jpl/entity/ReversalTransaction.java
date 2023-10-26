package com.vsoft.moneytransf.jpl.entity;

import com.vsoft.moneytransf.dto.TransactionDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Entity
@DiscriminatorValue("REVERSAL")
@Data
@Generated
@EqualsAndHashCode(callSuper = true)
public class ReversalTransaction extends Transaction{
    @ManyToOne
    @JoinColumn(name = "reference_id", referencedColumnName = "id")
    Transaction referencedTransaction;

    @Override
    public TransactionDTO toDTO() {
        var result = new TransactionDTO(getTimestamp(), getId(), getStatus(), getCustomerEmail(), getReferencedTransaction().getId());
        result.setType(TransactionDescriminator.REVERSAL);
        return result;
    }

}
