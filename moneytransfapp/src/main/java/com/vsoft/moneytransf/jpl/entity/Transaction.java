package com.vsoft.moneytransf.jpl.entity;

import com.vsoft.moneytransf.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Generated
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

    TransactionStatus status;
    @Column(name = "customer_email")
    String customerEmail;
    @Column(name = "customer_phone")
    String customerPhone;



    @ManyToOne
    @JoinColumn(name = "merchant_id", referencedColumnName = "id")
    Merchant merchant;

    @PrePersist
    protected void onCreate() {
        timestamp = System.currentTimeMillis();
    }

}
