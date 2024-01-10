package com.example.reactive.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class UserNotFoundException extends GlobalServiceException {
    public UserNotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }

    public UserNotFoundException(String reason, Map<String, Object> details) {
        super(HttpStatus.NOT_FOUND, reason, details);
    }

    public UserNotFoundException(HttpStatusCode status) {
        super(status);
    }

    public UserNotFoundException(HttpStatusCode status, Map<String, Object> details) {
        super(status, details);
    }

    public UserNotFoundException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public UserNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public UserNotFoundException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
