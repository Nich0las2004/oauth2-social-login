package com.secureauth.oauth2_social_login.service.user;

import com.secureauth.oauth2_social_login.entity.User;
import com.secureauth.oauth2_social_login.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OAuth2User principal;

    @InjectMocks
    private UserServiceImp userServiceImp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Save User - Facebook")
    void saveUser_Facebook() {
        when(principal.getAttribute("name")).thenReturn("John Doe");
        when(principal.getAttribute("id")).thenReturn("123456");
        when(principal.getAttribute("email")).thenReturn("john.doe@example.com");

        User savedUser = userServiceImp.saveUser(principal, "facebook");

        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        assertEquals("123456", savedUser.getFacebookUserId());
        assertEquals("john.doe@example.com", savedUser.getEmail());
        assertEquals("facebook", savedUser.getProvider());
        verify(userRepository, times(1)).save(savedUser);
    }

    @Test
    @DisplayName("Save User - GitHub")
    void saveUser_GitHub() {
        when(principal.getAttribute("name")).thenReturn("Jane Doe");
        when(principal.getAttribute("id")).thenReturn(654321);
        when(principal.getAttribute("login")).thenReturn("janedoe");
        when(principal.getAttribute("email")).thenReturn("jane.doe@example.com");

        User savedUser = userServiceImp.saveUser(principal, "github");

        assertNotNull(savedUser);
        assertEquals("Jane Doe", savedUser.getName());
        assertEquals(654321, savedUser.getGithubUserId());
        assertEquals("janedoe", savedUser.getUsername());
        assertEquals("jane.doe@example.com", savedUser.getEmail());
        assertEquals("github", savedUser.getProvider());
        verify(userRepository, times(1)).save(savedUser);
    }

    @Test
    @DisplayName("Register User")
    void registerUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        userServiceImp.registerUser(user);

        verify(userRepository, times(1)).save(user);
    }
}