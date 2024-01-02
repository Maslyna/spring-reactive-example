package com.example.reactive.exception.handler;

import com.example.reactive.exception.GlobalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GlobalServiceException.class)
    public ProblemDetail handleGlobalServiceException(GlobalServiceException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason() != null ? e.getReason() : e.getMessage()
        );
        problemDetail.setTitle("error");
        problemDetail.setProperties(e.details);
        return problemDetail;
    }
}
