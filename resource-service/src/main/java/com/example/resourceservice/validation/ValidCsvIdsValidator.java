package com.example.resourceservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCsvIdsValidator implements ConstraintValidator<ValidCsvIds, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return handleError(context, "CSV string cannot be null or empty");
        }

        if (value.length() > 200) {
            return handleError(context, String.format("CSV string is too long: received %d characters, maximum allowed is %d", value.length(), 200));
        }

        for (String id : value.split(",")) {
            if (!isValidId(id.trim())) {
                return handleError(context, String.format("Invalid ID format: '%s'. Only positive integers are allowed", id));
            }
        }

        return true;
    }

    private boolean isValidId(String id) {
        try {
            return Long.parseLong(id) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean handleError(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addConstraintViolation();

        return false;
    }
}
