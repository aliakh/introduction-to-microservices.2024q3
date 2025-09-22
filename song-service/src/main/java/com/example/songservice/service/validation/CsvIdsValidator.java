package com.example.songservice.service.validation;

import com.example.songservice.exception.InvalidIdException;
import org.springframework.stereotype.Component;

@Component
public class CsvIdsValidator {

    public void validate(String csv) {
        if (csv == null || csv.trim().isEmpty()) {
            throw new InvalidIdException("CSV string cannot be empty");
        }

        if (csv.length() > 200) {
            throw new InvalidIdException(String.format("CSV string is too long: received %d characters, maximum allowed is 200", csv.length()));
        }
    }
}
