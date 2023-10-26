package com.vsoft.moneytransf.jpl.entity;

import com.vsoft.moneytransf.dto.TransactionDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CHARGE")
@Data
@Generated
@EqualsAndHashCode(callSuper = true)
public class ChargeTransaction extends Transaction {
    @DecimalMin(value = "0", inclusive = false, message = "Amount must be greater than 0")
    BigDecimal amount;
    BigDecimal refundedAmount;
    @Override
    public TransactionDTO toDTO() {
        var result = new TransactionDTO(getTimestamp(), getId(), getStatus(), getCustomerEmail(), getAmount());
        result.setType(TransactionDescriminator.CHARGE);
        return result;
    }

}
