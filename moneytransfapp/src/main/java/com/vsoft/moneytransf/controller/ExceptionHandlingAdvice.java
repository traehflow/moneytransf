package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.exception.MerchantNotFoundException;
import com.vsoft.moneytransf.dto.ErrorOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler(MerchantNotFoundException.class)
    public ResponseEntity<ErrorOutput> handleSpecificException(MerchantNotFoundException ex) {
        ErrorOutput errorOutput = new ErrorOutput();
        errorOutput.setErrorMessage("Merchant not found");
        return new ResponseEntity<>(errorOutput, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputDataException.class)
    public ResponseEntity<ErrorOutput> handleSpecificException(InvalidInputDataException ex) {
        ErrorOutput errorOutput = new ErrorOutput();
        errorOutput.setErrorMessage("Invalid input");
        return new ResponseEntity<>(errorOutput, HttpStatus.BAD_REQUEST);
    }
}
