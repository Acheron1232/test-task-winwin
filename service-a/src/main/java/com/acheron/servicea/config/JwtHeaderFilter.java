package com.acheron.servicea.config;

import com.acheron.servicea.entity.User;
import com.acheron.servicea.exception.NotFoundException;
import com.acheron.servicea.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtHeaderFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws ServletException, IOException {
        assert filterChain != null;
        String jwtToken;
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            log.debug("no accessToken");

            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authorizationHeader.substring(7);
        if (!jwtToken.isBlank() && jwtUtil.validateToken(jwtToken)) {
            String username = jwtUtil.getUsername(jwtToken);
            User user;
            try {
                user = userService.findByEmail(username).cast();
            } catch (NotFoundException e) {
                log.debug("Token contains a non-existent user");
                assert response != null;
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token contains a non-existent user");
                filterChain.doFilter(request, response);
                return;
            }
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new Principal() {
                    @Override
                    public String getName() {
                        return user.getUsername();
                    }
                },
                        null,
                        user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        } else {
            log.debug("Token value is blank");
        }
        filterChain.doFilter(request, response);
    }
}