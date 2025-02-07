package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.entity.User;
import com.secureauth.oauth2_social_login.repository.UserRepository;
import com.secureauth.oauth2_social_login.service.notification.NotificationService;
import com.secureauth.oauth2_social_login.service.otp.OneTimePasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.ui.Model;


import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OneTimePasswordControllerTest {
    @Mock
    private OneTimePasswordService oneTimePasswordService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private User user;

    @Mock
    private Model model;

    @InjectMocks
    private OneTimePasswordController oneTimePasswordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Create OTP")
    void createOTPForm() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        User user = new User();
        user.setEmail("testuser@example.com");
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(oneTimePasswordService.returnOneTimePassword()).thenReturn("123456");

        String viewName = oneTimePasswordController.createOTPForm(model);

        assertEquals("otp", viewName);
        verify(notificationService).notifyUser(eq("testuser@example.com"), eq("Your One-Time Password (OTP) is: 123456"));
        verify(model).addAttribute("message", "OTP sent successfully to testuser@example.com");
    }

    @Test
    @DisplayName("Validate OTP")
    void validateOTP() {
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                "testuser",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(oneTimePasswordService.validateOneTimePassword("123456")).thenReturn(true);

        String viewName = oneTimePasswordController.validateOTP("123456", model);

        assertEquals("redirect:/dashboard", viewName);
        verify(model).addAttribute("message", "OTP is valid");
        verify(model).addAttribute("user", user);
    }

    @Test
    @DisplayName("Invalid OTP")
    void validateInvalidOTP() {
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                "testuser",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(oneTimePasswordService.validateOneTimePassword("invalid")).thenReturn(false);

        String viewName = oneTimePasswordController.validateOTP("invalid", model);

        assertEquals("otp", viewName);
        verify(model).addAttribute("error", "OTP is invalid");
    }

    @Test
    @DisplayName("Exception during OTP validation")
    void validateOTPException() {
        // Arrange
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                "testuser",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(oneTimePasswordService.validateOneTimePassword("123456")).thenThrow(new RuntimeException("Validation error"));

        String viewName = oneTimePasswordController.validateOTP("123456", model);

        assertEquals("otp", viewName);
        verify(model).addAttribute("error", "Failed to validate OTP");
    }

    @Test
    @DisplayName("Null OTP")
    void validateNullOTP() {
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                "testuser",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        String viewName = oneTimePasswordController.validateOTP(null, model);

        assertEquals("otp", viewName);
        verify(model).addAttribute("error", "OTP is invalid");
    }
}