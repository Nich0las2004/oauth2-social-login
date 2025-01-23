package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.entity.User;
import com.secureauth.oauth2_social_login.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubController {

    private final UserService userService;

    public GitHubController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/github")
    public String hello(@AuthenticationPrincipal OAuth2User principal) {
        String provider = "github";
        User user = userService.saveUser(principal, provider);
        return "User profile saved: " + user.getName();
    }

}
