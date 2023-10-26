package com.vsoft.moneytransf.jpl.entity;

import com.vsoft.moneytransf.dto.TransactionDTO;
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

    @Override
    public TransactionDTO toDTO() {
        TransactionDTO result = new TransactionDTO(getTimestamp(), getId(), getStatus(), getCustomerEmail(), getAmount());
        result.setType(TransactionDescriminator.AUTHORIZE);
        return result;
    }
}
