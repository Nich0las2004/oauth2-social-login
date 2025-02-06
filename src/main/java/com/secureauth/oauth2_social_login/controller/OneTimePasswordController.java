package com.secureauth.oauth2_social_login.controller;

import com.secureauth.oauth2_social_login.dto.NotificationRequest;
import com.secureauth.oauth2_social_login.repository.UserRepository;
import com.secureauth.oauth2_social_login.service.notification.NotificationService;
import com.secureauth.oauth2_social_login.service.otp.OneTimePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class OneTimePasswordController {

    private final OneTimePasswordService oneTimePasswordService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Autowired
    public OneTimePasswordController(OneTimePasswordService oneTimePasswordService, NotificationService notificationService, UserRepository userRepository) {
        this.oneTimePasswordService = oneTimePasswordService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/otp/create")
    public String createOTPForm(Model model) {
            try {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String username = auth.getName();

                com.secureauth.oauth2_social_login.entity.User user = userRepository.findByUsername(username);

                String email = user.getEmail();

                String otp = oneTimePasswordService.returnOneTimePassword();
                String message = "Your One-Time Password (OTP) is: " + otp;

                NotificationRequest notificationRequest = new NotificationRequest(email, message);
                notificationService.notifyUser(notificationRequest.getEmail(), notificationRequest.getContent());

                model.addAttribute("message", "OTP sent successfully to " + email);
            } catch (Exception exception) {
                model.addAttribute("error", "Failed to send OTP");
            }
        return "otp";
    }

    @PostMapping("/otp/validate")
    public String validateOTP(@RequestParam String otp, Model model) {
        try {
            boolean isValid = oneTimePasswordService.validateOneTimePassword(otp);

            if (isValid) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) authentication.getPrincipal();

                model.addAttribute("message", "OTP is valid");

                model.addAttribute("user", user);

                return "redirect:/dashboard";
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
