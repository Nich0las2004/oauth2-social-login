package com.secureauth.oauth2_social_login.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private static final Logger logger = LoggerFactory.getLogger(LoginAttemptService.class);
    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_TIME_DURATION = 10 * 60 * 1000;
    private final ConcurrentHashMap<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String username){
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null) {
            return false;
        }
        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            long blockTimeLeft = BLOCK_TIME_DURATION - (Instant.now().toEpochMilli() - attempt.getLastAttemptTime());
            if (blockTimeLeft > 0) {
                logger.warn("User {} is blocked due to too many failed login attempts.", username);
                return true;
            } else {
                resetAttempts(username);
                return false;
            }
        }
        return false;
    }

    public void loginFailed(String username) {
        loginAttempts.compute(username, (key, attempt) -> {
            if (attempt == null) {
                attempt = new LoginAttempt(username);
            }
            attempt.incrementAttempts();
            attempt.setLastAttemptTime(Instant.now().toEpochMilli());

            logger.warn("Failed login attempt {} for user {}", attempt.getAttempts(), username);

            return attempt;
        });
    }

    public void loginSucceeded(String username) {
        logger.info("User {} successfully logged in.", username);
        resetAttempts(username);
    }

    private void resetAttempts(String username) {
        loginAttempts.remove(username);
    }

    private static class LoginAttempt {
        private String username;
        private int attempts;
        private long lastAttemptTime;

        public LoginAttempt(String username) {
            this.username = username;
            this.attempts = 0;
            this.lastAttemptTime = Instant.now().toEpochMilli();
        }

        public void incrementAttempts() {
            this.attempts++;
        }

        public int getAttempts() {
            return attempts;
        }

        public long getLastAttemptTime() {
            return lastAttemptTime;
        }

        public void setLastAttemptTime(long lastAttemptTime) {
            this.lastAttemptTime = lastAttemptTime;
        }
    }
}
