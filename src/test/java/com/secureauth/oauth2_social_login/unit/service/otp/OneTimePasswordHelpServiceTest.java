package com.secureauth.oauth2_social_login.unit.service.otp;

import com.secureauth.oauth2_social_login.service.otp.OneTimePasswordHelpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class OneTimePasswordHelpServiceTest {

    @InjectMocks
    private OneTimePasswordHelpService oneTimePasswordHelpService;

    @BeforeEach
    void setUp() {
        oneTimePasswordHelpService = new OneTimePasswordHelpService();
    }

    @Test
    @DisplayName("Create random one time password")
    void createRandomOneTimePassword() {

        Supplier<Integer> supplier = oneTimePasswordHelpService.createRandomOneTimePassword();
        Integer otp = supplier.get();

        assertTrue(otp >= 100000 && otp <= 999999, "OTP should be a 6-digit number");
    }

    @Test
    @DisplayName("OTP is exactly 6 digits")
    void otpIsExactlySixDigits() {
        Supplier<Integer> supplier = oneTimePasswordHelpService.createRandomOneTimePassword();
        Integer otp = supplier.get();
        assertEquals(6, otp.toString().length(), "OTP should be exactly 6 digits");
    }
}