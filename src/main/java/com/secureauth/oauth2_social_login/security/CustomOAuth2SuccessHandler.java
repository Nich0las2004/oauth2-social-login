package com.secureauth.oauth2_social_login.security;

import com.secureauth.oauth2_social_login.model.AuthProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();

        String github = AuthProvider.github.toString();
        String facebook = AuthProvider.facebook.toString();

        if (github.equals(registrationId)) {
            response.sendRedirect("/github");
        } else if (facebook.equals(registrationId)) {
            response.sendRedirect("/facebook");
        } else {
            response.sendRedirect("/");
        }
    }
}