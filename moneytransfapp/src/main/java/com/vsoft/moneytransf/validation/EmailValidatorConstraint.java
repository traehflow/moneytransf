package com.vsoft.moneytransf.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.EmailValidator;

public class EmailValidatorConstraint implements
        ConstraintValidator<Email, String> {

    @Override
    public void initialize(Email email) {
    }

    @Override
    public boolean isValid(String email,
                           ConstraintValidatorContext cxt) {
        return StringUtils.isEmpty(email) || EmailValidator.getInstance().isValid(email);
    }

}