package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.service.otp.OneTimePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OneTimePasswordController {

    private final OneTimePasswordService oneTimePasswordService;

    @Autowired
    public OneTimePasswordController(OneTimePasswordService oneTimePasswordService) {
        this.oneTimePasswordService = oneTimePasswordService;
    }

    @GetMapping("/otp/create")
    private Object getOneTimePassword() {
        try {
            return ResponseEntity.ok(oneTimePasswordService.returnOneTimePassword());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
    }
}
