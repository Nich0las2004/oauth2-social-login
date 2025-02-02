package com.secureauth.oauth2_social_login.service.otp;

public interface OneTimePasswordService {
    String returnOneTimePassword();
    boolean validateOneTimePassword(String otp);
}
