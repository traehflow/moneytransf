package com.vsoft.moneytransf.jpl.entity;

import com.vsoft.moneytransf.dto.TransactionDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@DiscriminatorValue("REFUND")
@Data
@Generated
@EqualsAndHashCode(callSuper = true)
public class RefundTransaction extends Transaction {
    @DecimalMin(value = "0", inclusive = false, message = "Amount must be greater than 0")
    BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "reference_id", referencedColumnName = "id")
    Transaction referencedTransaction;

    @Override
    public TransactionDTO toDTO() {
        var result = new TransactionDTO(getTimestamp(), getId(), getStatus(), getCustomerEmail(), getReferencedTransaction().getId(), getAmount());
        result.setType(TransactionDescriminator.REFUND);
        return result;
    }

}
