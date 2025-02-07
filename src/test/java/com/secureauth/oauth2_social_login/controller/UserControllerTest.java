package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.dto.UserDTO;
import com.secureauth.oauth2_social_login.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

class UserControllerTest {

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test Register Form")
    void showRegisterForm() {
        String viewName = userController.showRegisterForm(model);
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("user"), any());
    }

    @Test
    @DisplayName("Test Registering a User")
    void register() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@example.com");
        userDTO.setPassword("password");

        when(bCryptPasswordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

        String viewName = userController.register(userDTO);
        assertEquals("register-success", viewName);

        verify(userService).registerUser(any());
    }
}