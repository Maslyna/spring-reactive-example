package com.example.reactive.router.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(
        String message
) {
}
