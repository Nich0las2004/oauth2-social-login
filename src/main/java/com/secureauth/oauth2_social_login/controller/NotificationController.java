package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.dto.NotificationRequest;
import com.secureauth.oauth2_social_login.service.notification.NotificationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public void createNotification(@Valid @RequestBody NotificationRequest request) {
        this.notificationService.notifyUser(request.getEmail(), request.getContent());
    }
}
