package com.vsoft.moneytransf.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.EmailValidator;

public class EmailValidatorConstraint implements
        ConstraintValidator<Email, String> {

    @Override
    public void initialize(Email contactNumber) {
    }

    @Override
    public boolean isValid(String email,
                           ConstraintValidatorContext cxt) {
        return EmailValidator.getInstance().isValid(email);
    }

}