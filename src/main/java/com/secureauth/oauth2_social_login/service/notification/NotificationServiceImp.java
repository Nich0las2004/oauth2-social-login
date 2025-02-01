package com.secureauth.oauth2_social_login.service.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImp implements NotificationService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public NotificationServiceImp(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void notifyUser(String email, String content) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("admin@spring.io");
        mail.setSubject("A new message for you");
        mail.setText(content);
        mail.setTo(email);

        System.out.println("📧 Mock Email Sent to: " + email);
        System.out.println("Message: " + content);

        this.javaMailSender.send(mail);
    }
}
