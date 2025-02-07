package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.entity.User;
import com.secureauth.oauth2_social_login.model.AuthProvider;
import com.secureauth.oauth2_social_login.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GitHubControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private OAuth2User principal;

    @Mock
    private Model model;

    @InjectMocks
    private GitHubController gitHubController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveGitHubUserProfile() {
        User user = new User();
        when(userService.saveUser(principal, String.valueOf(AuthProvider.github))).thenReturn(user);

        String viewName = gitHubController.saveGitHubUserProfile(principal, model);

        assertEquals("github", viewName);
        verify(model).addAttribute("user", user);
    }
}