package com.citypulse.application.dto.response;

import com.citypulse.domain.enums.Role;

public record AuthResponse(
        String token,
        String refreshToken,
        String username,
        String email,
        Role role
) {}
