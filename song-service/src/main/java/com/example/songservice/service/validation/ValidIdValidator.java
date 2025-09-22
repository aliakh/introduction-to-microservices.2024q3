package com.example.songservice.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidIdValidator implements ConstraintValidator<ValidId, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null || value <= 0) {
            context.disableDefaultConstraintViolation();

            var message = String.format("Invalid value '%d' for ID. Must be a positive integer", value);
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

            return false;
        } else {
            return true;
        }
    }
}
