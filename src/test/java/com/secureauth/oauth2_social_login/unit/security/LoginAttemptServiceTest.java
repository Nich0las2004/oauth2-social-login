package com.secureauth.oauth2_social_login.unit.security;

import com.secureauth.oauth2_social_login.security.LoginAttemptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginAttemptServiceTest {

    private LoginAttemptService loginAttemptService;

    @BeforeEach
    void setUp() {
        loginAttemptService = new LoginAttemptService();
    }

    @Test
    @DisplayName("User is not blocked initially")
    void isBlocked_UserNotBlockedInitially() {
        assertFalse(loginAttemptService.isBlocked("testuser"));
    }

    @Test
    @DisplayName("User is blocked after max failed attempts")
    void isBlocked_UserBlockedAfterMaxFailedAttempts() {
        String username = "testuser";
        for (int i = 0; i < 3; i++) {
            loginAttemptService.loginFailed(username);
        }
        assertTrue(loginAttemptService.isBlocked(username));
    }

    @Test
    @DisplayName("User is not blocked after successful login")
    void isBlocked_UserNotBlockedAfterSuccessfulLogin() {
        String username = "testuser";
        for (int i = 0; i < 3; i++) {
            loginAttemptService.loginFailed(username);
        }
        loginAttemptService.loginSucceeded(username);
        assertFalse(loginAttemptService.isBlocked(username));
    }

    @Test
    @DisplayName("Failed login attempts are reset after block time")
    void isBlocked_FailedAttemptsResetAfterBlockTime() {
        String username = "testuser";
        for (int i = 0; i < 3; i++) {
            loginAttemptService.loginFailed(username);
        }

        try (MockedStatic<Instant> mockedInstant = mockStatic(Instant.class, Mockito.CALLS_REAL_METHODS)) {
            Instant futureTime = Instant.now().plusMillis(10 * 60 * 1000 + 1000);
            mockedInstant.when(Instant::now).thenReturn(futureTime);

            assertFalse(loginAttemptService.isBlocked(username));
        }
    }

}