package com.example.reactive.router.response;

import com.example.reactive.entity.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountResponse(
        UUID userId,
        String username,
        Role role
) {
}
