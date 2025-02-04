package com.secureauth.oauth2_social_login.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RateLimitAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitAuthenticationFilter.class);
    private final LoginAttemptService loginAttemptService;
    private final AuthenticationManager authenticationManager;

    public RateLimitAuthenticationFilter(LoginAttemptService loginAttemptService, AuthenticationManager authenticationManager) {
        this.loginAttemptService = loginAttemptService;
        this.authenticationManager = authenticationManager;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/login".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String username = request.getParameter("username");

            logger.info("RateLimitAuthenticationFilter triggered for username: {}", username);

            if (username != null && loginAttemptService.isBlocked(username)) {
                logger.warn("User is blocked: {}", username);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Too many failed login attempts. Please try again later.");
                return;
            }

            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, request.getParameter("password"))
                );

                if (authentication.isAuthenticated()) {
                    loginAttemptService.loginSucceeded(username);
                    logger.info("User {} successfully authenticated", username);
                }
            } catch (AuthenticationException e) {
                loginAttemptService.loginFailed(username);
                logger.warn("Failed login attempt for user {}", username);
                throw e;
            }
        }

        filterChain.doFilter(request, response);
    }
}
