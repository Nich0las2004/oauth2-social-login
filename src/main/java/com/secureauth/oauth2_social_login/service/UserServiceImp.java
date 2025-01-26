package com.secureauth.oauth2_social_login.service;

import com.secureauth.oauth2_social_login.entity.User;
import com.secureauth.oauth2_social_login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(@AuthenticationPrincipal OAuth2User principal, String provider) {

        User newUser = new User();

        newUser.setName(principal.getAttribute("name"));
        if(provider.equals("facebook"))
            newUser.setFacebookUserId(principal.getAttribute("id"));
        else if(provider.equals("github")){
            newUser.setGithubUserId(principal.getAttribute("id"));
        }
        newUser.setProvider(provider);
        newUser.setEmail(principal.getAttribute("email"));

        userRepository.save(newUser);

        return newUser;
    }

    public void registerUser(User user) {
        userRepository.save(user);
    }

}
