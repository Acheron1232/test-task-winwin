package com.acheron.servicea.service;

import com.acheron.servicea.api.AuthApi;
import com.acheron.servicea.config.JwtUtil;
import com.acheron.servicea.dto.UserDto;
import com.acheron.servicea.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistration_SuccessfulRegistration() {
        AuthApi.AuthDto authDto = new AuthApi.AuthDto("newuser@example.com", "password123");
        User savedUser = new User(UUID.randomUUID(), "newuser@example.com", "encodedPassword", User.Role.USER);

        when(userService.save(authDto)).thenReturn(savedUser);

        ResponseEntity<Void> response = authenticationService.registration(authDto);

        assertEquals(201, response.getStatusCode().value());
        verify(userService).save(authDto);
    }
}
