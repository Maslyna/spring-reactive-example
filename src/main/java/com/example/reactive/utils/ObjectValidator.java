package com.example.reactive.utils;

import com.example.reactive.exception.ObjectValidationException;
import jakarta.validation.Validator;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Validated
@Component
@RequiredArgsConstructor
public class ObjectValidator {

    private final Validator validator;

    public <T> T validate(T object) {
        var errors = validator.validate(object);
        if (errors.isEmpty()) {
            return object;
        } else {
            Map<String, Object> details = Map.of(
                    "errors",
                    errors.stream().map(err -> ErrorMessage.builder().message(err.getMessage())
                            .invalidValue(err.getInvalidValue()).build())
                            .toList()
            );
            throw new ObjectValidationException(HttpStatus.BAD_REQUEST, details);
        }
    }

    @Builder
    private static record ErrorMessage(
            String message,
            Object invalidValue
    ) {
    }
}