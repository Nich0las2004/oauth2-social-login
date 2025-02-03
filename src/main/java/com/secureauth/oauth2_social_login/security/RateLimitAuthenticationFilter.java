package com.secureauth.oauth2_social_login.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class RateLimitAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final LoginAttemptService loginAttemptService;

    public RateLimitAuthenticationFilter(LoginAttemptService loginAttemptService, AuthenticationManager authenticationManager) {
        this.loginAttemptService = loginAttemptService;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!httpRequest.getServletPath().equals("/login")) {
            chain.doFilter(request, response);
            return;
        }

        String username = httpRequest.getParameter("username");

        System.out.println("RateLimitAuthenticationFilter triggered for username: " + username);

        if (username != null && loginAttemptService.isBlocked(username)) {
            System.out.println("User is blocked: " + username);
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("Too many failed login attempts. Please try again later.");
            return;
        }

        super.doFilter(request, response, chain);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, org.springframework.security.core.Authentication authResult) throws IOException, ServletException {
        String username = authResult.getName();
        loginAttemptService.loginSucceeded(username);
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String username = request.getParameter("username");

        if (username != null) {
            System.out.println("Failed login attempt for user: " + username);
            loginAttemptService.loginFailed(username);
        }

        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");

        if (username != null && loginAttemptService.isBlocked(username)) {
            throw new LockedException("Too many failed login attempts. Try again later.");
        }

        return super.attemptAuthentication(request, response);
    }
}
