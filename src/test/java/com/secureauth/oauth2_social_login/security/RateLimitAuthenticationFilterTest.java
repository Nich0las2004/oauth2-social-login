package com.secureauth.oauth2_social_login.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimitAuthenticationFilterTest {

    private LoginAttemptService loginAttemptService;
    private AuthenticationManager authenticationManager;
    private RateLimitAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        loginAttemptService = mock(LoginAttemptService.class);
        authenticationManager = mock(AuthenticationManager.class);
        filter = new RateLimitAuthenticationFilter(loginAttemptService, authenticationManager);
    }

    @Test
    @DisplayName("User is blocked")
    void doFilterInternal_UserBlocked() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(request.getServletPath()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("username")).thenReturn("testuser");
        when(loginAttemptService.isBlocked("testuser")).thenReturn(true);
        when(response.getWriter()).thenReturn(writer);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(writer).write("Too many failed login attempts. Please try again later.");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("User is not blocked and authentication succeeds")
    void doFilterInternal_UserNotBlocked_AuthenticationSucceeds() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getServletPath()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("password");
        when(loginAttemptService.isBlocked("testuser")).thenReturn(false);

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        filter.doFilterInternal(request, response, filterChain);

        verify(loginAttemptService).loginSucceeded("testuser");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("User is not blocked and authentication fails")
    void doFilterInternal_UserNotBlocked_AuthenticationFails() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getServletPath()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("password");
        when(loginAttemptService.isBlocked("testuser")).thenReturn(false);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Authentication failed") {});

        assertThrows(AuthenticationException.class, () -> filter.doFilterInternal(request, response, filterChain));

        verify(loginAttemptService).loginFailed("testuser");
        verify(filterChain, never()).doFilter(request, response);
    }

}