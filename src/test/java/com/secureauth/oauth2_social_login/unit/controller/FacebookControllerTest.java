package com.secureauth.oauth2_social_login.unit.controller;

import com.secureauth.oauth2_social_login.controller.FacebookController;
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

class FacebookControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private OAuth2User principal;

    @Mock
    private Model model;

    @InjectMocks
    private FacebookController facebookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveFacebookUserProfile() {
        User user = new User();
        when(userService.saveUser(principal, String.valueOf(AuthProvider.facebook))).thenReturn(user);

        String viewName = facebookController.saveFacebookUserProfile(principal, model);

        assertEquals("facebook", viewName);
        verify(model).addAttribute("user", user);
    }
}