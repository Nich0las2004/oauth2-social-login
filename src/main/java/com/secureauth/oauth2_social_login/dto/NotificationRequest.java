package com.secureauth.oauth2_social_login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NotificationRequest {
    @Email
    private String email;

    @NotBlank
    private String content;

    public NotificationRequest() {
    }

    public NotificationRequest(String email, String content) {
        this.email = email;
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
