package com.acheron.servicea.service;

import com.acheron.servicea.api.AuthApi;
import com.acheron.servicea.entity.User;
import com.acheron.servicea.exception.NotFoundException;
import com.acheron.servicea.exception.UserAlreadyExistsException;
import com.acheron.servicea.exception.ValidationException;
import com.acheron.servicea.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByEmail_UserFound() {
        User user = new User(UUID.randomUUID(), "test@example.com", "password", User.Role.USER);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        var userDto = userService.findByEmail("test@example.com");

        assertNotNull(userDto);
        assertEquals("test@example.com", userDto.email());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testFindByEmail_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findByEmail("test@example.com"));
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        User user = new User(UUID.randomUUID(), "test@example.com", "password", User.Role.USER);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        var userDetails = userService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.loadUserByUsername("test@example.com"));
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testSave_SuccessfulRegistration() {
        AuthApi.AuthDto authDto = new AuthApi.AuthDto("newuser@example.com", "password123");
        User savedUser = new User(UUID.randomUUID(), "newuser@example.com", "encodedPassword", User.Role.USER);

        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.save(authDto);

        assertNotNull(result);
        assertEquals("newuser@example.com", result.getEmail());
        verify(userRepository).existsByEmail("newuser@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSave_UserAlreadyExists() {
        AuthApi.AuthDto authDto = new AuthApi.AuthDto("existing@example.com", "password123");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.save(authDto));
        verify(userRepository).existsByEmail("existing@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testSave_EmptyPassword() {
        AuthApi.AuthDto authDto = new AuthApi.AuthDto("user@example.com", "");

        assertThrows(ValidationException.class, () -> userService.save(authDto));
        verify(userRepository, never()).existsByEmail(anyString());
    }
}