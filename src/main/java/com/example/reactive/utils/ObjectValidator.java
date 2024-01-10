package com.example.reactive.utils;

import com.example.reactive.exception.ObjectValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ObjectValidator {

    private final Validator validator;

    public <T> T validate(T object) {
        var errors = validator.validate(object);
        if (errors.isEmpty()) {
            return object;
        } else {
            String errorDetails = errors.stream().map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new ObjectValidationException(HttpStatus.BAD_REQUEST, errorDetails);
        }
    }
}