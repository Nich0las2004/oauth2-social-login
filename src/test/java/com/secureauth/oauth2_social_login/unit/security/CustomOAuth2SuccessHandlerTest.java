package com.secureauth.oauth2_social_login.unit.security;

import com.secureauth.oauth2_social_login.model.AuthProvider;
import com.secureauth.oauth2_social_login.security.CustomOAuth2SuccessHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;


class CustomOAuth2SuccessHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2AuthenticationToken oauthToken;

    @Mock
    private OAuth2User oauth2User;

    @InjectMocks
    private CustomOAuth2SuccessHandler successHandler;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(successHandler).build();
    }

    @Test
    @DisplayName("Redirect to /github on GitHub authentication success")
    void onAuthenticationSuccess_GitHub() throws Exception {
        when(oauthToken.getAuthorizedClientRegistrationId()).thenReturn(AuthProvider.github.toString());
        when(authentication.getPrincipal()).thenReturn(oauth2User);

        successHandler.onAuthenticationSuccess(request, response, oauthToken);

        verify(response).sendRedirect("/github");
    }

    @Test
    @DisplayName("Redirect to /facebook on Facebook authentication success")
    void onAuthenticationSuccess_Facebook() throws Exception {
        when(oauthToken.getAuthorizedClientRegistrationId()).thenReturn(AuthProvider.facebook.toString());
        when(authentication.getPrincipal()).thenReturn(oauth2User);

        successHandler.onAuthenticationSuccess(request, response, oauthToken);

        verify(response).sendRedirect("/facebook");
    }

    @Test
    @DisplayName("Redirect to / on other authentication success")
    void onAuthenticationSuccess_Other() throws Exception {
        when(oauthToken.getAuthorizedClientRegistrationId()).thenReturn("other");
        when(authentication.getPrincipal()).thenReturn(oauth2User);

        successHandler.onAuthenticationSuccess(request, response, oauthToken);

        verify(response).sendRedirect("/");
    }
}