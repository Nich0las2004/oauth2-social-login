package com.secureauth.oauth2_social_login.service.otp;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.function.Supplier;

@Service
public class OneTimePasswordHelpService {
    public Supplier<Integer> createRandomOneTimePassword() {
        return () -> {
            Random random = new Random();
            return 100000 + random.nextInt(900000);
        };
    }
}
