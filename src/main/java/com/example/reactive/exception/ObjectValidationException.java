package com.example.reactive.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class ObjectValidationException extends GlobalServiceException {

    public ObjectValidationException(HttpStatusCode status) {
        super(status);
    }

    public ObjectValidationException(HttpStatusCode status, Map<String, Object> details) {
        super(status, details);
    }

    public ObjectValidationException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public ObjectValidationException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public ObjectValidationException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
