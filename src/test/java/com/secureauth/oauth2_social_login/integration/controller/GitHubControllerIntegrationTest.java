package com.secureauth.oauth2_social_login.integration.controller;

import com.secureauth.oauth2_social_login.controller.GitHubController;
import com.secureauth.oauth2_social_login.entity.User;
import com.secureauth.oauth2_social_login.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(GitHubController.class)
public class GitHubControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser
    public void testSaveGitHubUserProfile() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setName("Test User");
        user.setEmail("testuser@example.com");

        Map<String, Object> attributes = Map.of(
                "name", "Test User",
                "email", "testuser@example.com"
        );
        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "name"
        );

        when(userService.saveUser(oAuth2User, "github")).thenReturn(user);

        mockMvc.perform(get("/github").with(oauth2Login().oauth2User(oAuth2User)))
                .andExpect(status().isOk())
                .andExpect(view().name("github"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user));
    }
}