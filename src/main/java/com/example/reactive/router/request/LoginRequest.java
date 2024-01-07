package com.example.reactive.router.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record LoginRequest (
        @Email
        String login,
        @Size(min = 8, max = 16)
        String password
) {
}
