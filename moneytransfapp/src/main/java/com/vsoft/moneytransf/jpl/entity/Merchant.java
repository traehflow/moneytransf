package com.vsoft.moneytransf.jpl.entity;

import com.vsoft.moneytransf.MerchantStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Currency;
import java.util.UUID;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "merchant",
        indexes = {@Index(name = "idx_email", columnList = "email")})
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name="name")
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="email", unique = true)
    private String email;
    @Column(name = "status")
    private MerchantStatus status;
    @Column(name = "total_transaction_sum")
    private Currency totalTransactionSum;

}
