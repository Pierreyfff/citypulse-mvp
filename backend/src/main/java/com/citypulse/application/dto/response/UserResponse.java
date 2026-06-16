package com.citypulse.application.dto.response;

import com.citypulse.domain.enums.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        boolean active,
        LocalDateTime createdAt
) {}
