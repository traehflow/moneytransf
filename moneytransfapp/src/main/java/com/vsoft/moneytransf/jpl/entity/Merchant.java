package com.vsoft.moneytransf.jpl.entity;

import com.vsoft.moneytransf.MerchantStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Currency;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "merchant")
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="email")
    private String email;
    @Column(name = "status")
    private MerchantStatus status;
    @Column(name = "total_transaction_sum")
    private Currency totalTransactionSum;

}
