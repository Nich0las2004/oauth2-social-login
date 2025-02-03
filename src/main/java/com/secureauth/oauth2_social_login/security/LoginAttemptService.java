package com.secureauth.oauth2_social_login.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 1;
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
            return attempt;
        });
    }

    public void loginSucceeded(String username) {
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
