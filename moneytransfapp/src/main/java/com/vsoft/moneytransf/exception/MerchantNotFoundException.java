package com.vsoft.moneytransf.exception;

public class MerchantNotFoundException extends RuntimeException {
    public MerchantNotFoundException(Exception e) {
        super(e);
    }

    public MerchantNotFoundException() {
        super();
    }
}
