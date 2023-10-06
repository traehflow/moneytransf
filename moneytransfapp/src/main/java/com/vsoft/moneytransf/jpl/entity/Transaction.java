package com.vsoft.moneytransf.jpl.entity;

import com.vsoft.moneytransf.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transaction",
        indexes = {@Index(name = "idx_timestamp", columnList = "timestamp")}
)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="transaction_type",
        discriminatorType=DiscriminatorType.STRING
)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @Temporal(TemporalType.TIMESTAMP)
    long timestamp;

    @DecimalMin(value = "0", inclusive = false, message = "Amount must be greater than 0")
    BigDecimal amount;
    TransactionStatus status;
    @Column(name = "customer_email")
    String customerEmail;
    @Column(name = "customer_phone")
    String customerPhone;
    @Column(name = "reference_id")
    String referenceId;

    @PrePersist
    protected void onCreate() {
        timestamp = System.currentTimeMillis();
    }

}
