package com.citypulse.infrastructure.persistence.mapper;

import com.citypulse.domain.model.User;
import com.citypulse.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        Optional.ofNullable(user.getId()).ifPresent(entity::setId);
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        entity.setActive(user.isActive());
        Optional.ofNullable(user.getCreatedAt()).ifPresent(entity::setCreatedAt);
        Optional.ofNullable(user.getUpdatedAt()).ifPresent(entity::setUpdatedAt);
        return entity;
    }

    public User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
