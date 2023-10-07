package com.vsoft.moneytransf.exception;

public class InvalidInputDataException extends RuntimeException{
    public InvalidInputDataException(Exception ex) {
        super(ex);
    }
}
