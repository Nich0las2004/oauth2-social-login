package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.entity.User;
import com.secureauth.oauth2_social_login.model.AuthProvider;
import com.secureauth.oauth2_social_login.service.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GitHubController {

    private final UserService userService;

    public GitHubController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/github")
    public String saveGitHubUserProfile(@AuthenticationPrincipal OAuth2User principal, Model model) {
        User user = userService.saveUser(principal, String.valueOf(AuthProvider.github));
        model.addAttribute("user", user);

        return "github";
    }

}
