package com.secureauth.oauth2_social_login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubController {

    @GetMapping("/")
    public String hello(){
        return "Hello Github";
    }

}
