package com.secureauth.oauth2_social_login.unit.service.notification;

import com.secureauth.oauth2_social_login.service.notification.NotificationServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class NotificationServiceImpTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationServiceImp notificationServiceImp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Notify User - Success")
    void notifyUser_Success() {
        String email = "test@example.com";
        String content = "This is a test message";
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        notificationServiceImp.notifyUser(email, content);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}