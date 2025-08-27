package com.acheron.servicea.service;

import com.acheron.servicea.api.AuthApi;
import com.acheron.servicea.config.JwtUtil;
import com.acheron.servicea.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ResponseEntity<String> login(AuthApi.AuthDto authDto) {
        log.info("Login attempt for email: {}", authDto.email());

        User user = userService.findByEmail(authDto.email()).cast();

        log.debug("User found: {}", user);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password())
        );
        log.info("Authentication successful for email: {}", authDto.email());

        String token = jwtUtil.generateToken(user);
        log.debug("Generated JWT token for {}: {}", authDto.email(), token);

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<Void> registration(AuthApi.AuthDto authDto) {
        log.info("Registration attempt for email: {}", authDto.email());

        User user = userService.save(authDto);

        log.debug("User successfully registered: {}", user);

        return ResponseEntity.status(201).build();
    }
}
