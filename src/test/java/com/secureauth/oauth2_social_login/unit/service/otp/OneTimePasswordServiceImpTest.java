package com.secureauth.oauth2_social_login.unit.service.otp;

import com.secureauth.oauth2_social_login.entity.OneTimePassword;
import com.secureauth.oauth2_social_login.repository.OneTimePasswordRepository;
import com.secureauth.oauth2_social_login.service.otp.OneTimePasswordHelpService;
import com.secureauth.oauth2_social_login.service.otp.OneTimePasswordServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OneTimePasswordServiceImpTest {
    @Mock
    private OneTimePasswordRepository oneTimePasswordRepository;

    @Mock
    private OneTimePasswordHelpService oneTimePasswordHelpService;

    @InjectMocks
    private OneTimePasswordServiceImp oneTimePasswordServiceImp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Return One Time Password")
    void returnOneTimePassword() {
        Supplier<Integer> otpSupplier = () -> 123456;
        when(oneTimePasswordHelpService.createRandomOneTimePassword()).thenReturn(otpSupplier);

        String otp = oneTimePasswordServiceImp.returnOneTimePassword();

        assertEquals("123456", otp);
        verify(oneTimePasswordRepository, times(1)).save(any(OneTimePassword.class));
    }

    @Test
    @DisplayName("Validate One Time Password - Success")
    void validateOneTimePassword_Success() {
        OneTimePassword otpEntity = new OneTimePassword();
        otpEntity.setOneTimePasswordCode("123456");
        otpEntity.setExpires(new Date(System.currentTimeMillis() + 60000)); // 1 minute in the future
        when(oneTimePasswordRepository.findByOneTimePasswordCode("123456")).thenReturn(Optional.of(otpEntity));

        boolean isValid = oneTimePasswordServiceImp.validateOneTimePassword("123456");

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Validate One Time Password - Failure")
    void validateOneTimePassword_Failure() {
        when(oneTimePasswordRepository.findByOneTimePasswordCode("123456")).thenReturn(Optional.empty());

        boolean isValid = oneTimePasswordServiceImp.validateOneTimePassword("123456");

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Validate One Time Password - Expired")
    void validateOneTimePassword_Expired() {
        OneTimePassword otpEntity = new OneTimePassword();
        otpEntity.setOneTimePasswordCode("123456");
        otpEntity.setExpires(new Date(System.currentTimeMillis() - 60000));
        when(oneTimePasswordRepository.findByOneTimePasswordCode("123456")).thenReturn(Optional.of(otpEntity));

        boolean isValid = oneTimePasswordServiceImp.validateOneTimePassword("123456");

        assertFalse(isValid);
    }
}