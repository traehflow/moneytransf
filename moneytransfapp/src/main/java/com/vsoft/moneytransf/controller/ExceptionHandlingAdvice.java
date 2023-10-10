package com.vsoft.moneytransf.controller;

import com.vsoft.moneytransf.exception.InvalidInputDataException;
import com.vsoft.moneytransf.exception.MerchantNotFoundException;
import com.vsoft.moneytransf.dto.ErrorOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        errorOutput.setErrorMessage("Invalid input ");
        return new ResponseEntity<>(errorOutput, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> body = new HashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(t -> String.join("", t.getField(), ": ", t.getDefaultMessage()))
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
