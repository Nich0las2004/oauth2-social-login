package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.dto.NotificationRequest;
import com.secureauth.oauth2_social_login.service.notification.NotificationService;
import com.secureauth.oauth2_social_login.service.otp.OneTimePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class OneTimePasswordController {

    private final OneTimePasswordService oneTimePasswordService;
    private final NotificationService notificationService;

    @Autowired
    public OneTimePasswordController(OneTimePasswordService oneTimePasswordService, NotificationService notificationService) {
        this.oneTimePasswordService = oneTimePasswordService;
        this.notificationService = notificationService;
    }

//    mock email: http://localhost:8080/otp/create?email=user@example.com

    @GetMapping("/otp/create")
    public String createOTPForm(@RequestParam(required = false) String email, Model model) {
        if (email != null && !email.isEmpty()) {
            try {
                String otp = oneTimePasswordService.returnOneTimePassword();
                String message = "Your One-Time Password (OTP) is: " + otp;

                NotificationRequest notificationRequest = new NotificationRequest(email, message);
                notificationService.notifyUser(notificationRequest.getEmail(), notificationRequest.getContent());

                model.addAttribute("message", "OTP sent successfully to " + email);
            } catch (Exception exception) {
                model.addAttribute("error", "Failed to send OTP");
            }
        }
        return "otp";
    }

    @PostMapping("/otp/validate")
    public String validateOTP(@RequestParam String otp, Model model) {
        try {
            boolean isValid = oneTimePasswordService.validateOneTimePassword(otp);

            if (isValid) {
                model.addAttribute("message", "OTP is valid");
                return "otp";
            } else {
                model.addAttribute("error", "OTP is invalid");
                return "otp";
            }

        } catch (Exception exception) {
            model.addAttribute("error", "Failed to validate OTP");
            return "otp";
        }
    }
}
