package com.acheron.servicea.service;

import com.acheron.servicea.api.AuthApi;
import com.acheron.servicea.dto.UserDto;
import com.acheron.servicea.entity.User;
import com.acheron.servicea.exception.NotFoundException;
import com.acheron.servicea.exception.UserAlreadyExistsException;
import com.acheron.servicea.exception.ValidationException;
import com.acheron.servicea.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto findByEmail(String email) {
        log.debug("Looking up user by email: {}", email);
        User user = findUserByEmail(email);
        log.debug("Found user: {}", user);
        return new UserDto(user.getId(), user.getEmail(), user.getRole());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new NotFoundException(email);
                });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        log.debug("Loading user by username: {}", username);
        return findUserByEmail(username);
    }

    public User save(AuthApi.AuthDto authDto) {
        log.info("Attempting to register user with email: {}", authDto.email());

        if (authDto.password() == null || authDto.password().isBlank()) {
            log.warn("Registration failed: password is empty for email {}", authDto.email());
            throw new ValidationException("Password should not be empty");
        }

        if (userRepository.existsByEmail(authDto.email())) {
            log.warn("Registration failed: user already exists with email {}", authDto.email());
            throw new UserAlreadyExistsException("User already exists");
        }


        User savedUser = userRepository.save(
                new User(null, authDto.email(), passwordEncoder.encode(authDto.password()), User.Role.USER)
        );

        log.info("User registered successfully with email: {}", savedUser.getEmail());
        return savedUser;
    }
}