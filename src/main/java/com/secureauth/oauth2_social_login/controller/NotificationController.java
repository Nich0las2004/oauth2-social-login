package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.dto.NotificationRequest;
import com.secureauth.oauth2_social_login.service.notification.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createNotification(@Valid @RequestBody NotificationRequest request) {
        try {
            this.notificationService.notifyUser(request.getEmail(), request.getContent());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Notification sent successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to send notification");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
