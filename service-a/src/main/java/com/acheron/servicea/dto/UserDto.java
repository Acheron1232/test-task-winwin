package com.acheron.servicea.dto;

import com.acheron.servicea.entity.User;

import java.util.UUID;


public record UserDto(
        UUID id, String email, User.Role role
) {

    public User cast() {
        return new User(this.id, this.email, null, this.role);
    }

    public UserDto castToDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getRole());
    }
}