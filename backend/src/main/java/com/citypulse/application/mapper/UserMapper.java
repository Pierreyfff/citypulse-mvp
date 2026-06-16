package com.citypulse.application.mapper;

import com.citypulse.application.dto.request.UserRequest;
import com.citypulse.application.dto.response.UserResponse;
import com.citypulse.domain.enums.Role;
import com.citypulse.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper {

    public User toDomain(UserRequest request) {
        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .role(Optional.ofNullable(request.role()).orElse(Role.CIUDADANO))
                .active(Optional.ofNullable(request.active()).orElse(true))
                .build();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
