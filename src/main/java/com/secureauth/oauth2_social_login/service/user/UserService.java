package com.secureauth.oauth2_social_login.service.user;


import com.secureauth.oauth2_social_login.entity.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {
    User saveUser(OAuth2User principal, String provider);
    void registerUser(User user);
}
