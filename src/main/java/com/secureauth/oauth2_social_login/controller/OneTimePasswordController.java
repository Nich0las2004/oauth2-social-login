package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.dto.NotificationRequest;
import com.secureauth.oauth2_social_login.service.notification.NotificationService;
import com.secureauth.oauth2_social_login.service.otp.OneTimePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OneTimePasswordController {

    private final OneTimePasswordService oneTimePasswordService;
    private final NotificationService notificationService;

    @Autowired
    public OneTimePasswordController(OneTimePasswordService oneTimePasswordService, NotificationService notificationService) {
        this.oneTimePasswordService = oneTimePasswordService;
        this.notificationService = notificationService;
    }

    @GetMapping("/otp/create")
    public ResponseEntity<Map<String, String>> createOTP(@RequestParam String email) {
        try {
            String otp = oneTimePasswordService.returnOneTimePassword();

            String message = "Your One-Time Password (OTP) is: " + otp;

            NotificationRequest notificationRequest = new NotificationRequest(email, message);
            notificationService.notifyUser(notificationRequest.getEmail(), notificationRequest.getContent());

            Map<String, String> response = new HashMap<>();
            response.put("message", "OTP sent successfully to " + email);
            return ResponseEntity.ok(response);

        } catch (Exception exception) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to send OTP");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
