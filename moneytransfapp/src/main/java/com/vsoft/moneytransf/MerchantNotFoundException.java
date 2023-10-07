package com.vsoft.moneytransf;

public class MerchantNotFoundException extends RuntimeException {
    public MerchantNotFoundException(Exception e) {
        super(e);
    }
}
