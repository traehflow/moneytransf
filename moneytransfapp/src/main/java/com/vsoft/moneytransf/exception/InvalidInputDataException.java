package com.vsoft.moneytransf.exception;

public class InvalidInputDataException extends RuntimeException{
    public InvalidInputDataException(Exception ex) {
        super(ex);
    }

    public InvalidInputDataException(String message, Exception ex) {
        super(message, ex);
    }

    public InvalidInputDataException(String message) {
        super(message);
    }
}
