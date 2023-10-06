package com.vsoft.moneytransf.jpl.entity;

public enum TransactionDescriminator {
    AUTHORIZE("AUTHORIZE"), CHARGE("CHARGE"), REFUND("REFUND"), REVERSAL("REVERSAL");

    TransactionDescriminator(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }

    private String value;


}
