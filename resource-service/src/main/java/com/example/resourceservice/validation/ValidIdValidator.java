package com.example.resourceservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidIdValidator implements ConstraintValidator<ValidId, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null || value <= 0) {
            return handleError(context, String.format("Invalid value '%d' for ID. Must be a positive integer", value));
        } else {
            return true;
        }
    }

    private boolean handleError(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addConstraintViolation();

        return false;
    }
}
