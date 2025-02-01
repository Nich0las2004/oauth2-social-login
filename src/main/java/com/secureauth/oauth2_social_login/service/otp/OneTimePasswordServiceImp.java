package com.secureauth.oauth2_social_login.service.otp;

import com.secureauth.oauth2_social_login.entity.OneTimePassword;
import com.secureauth.oauth2_social_login.repository.OneTimePasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OneTimePasswordServiceImp implements OneTimePasswordService{

    private final OneTimePasswordRepository oneTimePasswordRepository;

    private final OneTimePasswordHelpService oneTimePasswordHelpService;

    @Autowired
    public OneTimePasswordServiceImp(OneTimePasswordRepository oneTimePasswordRepository, OneTimePasswordHelpService oneTimePasswordHelpService) {
        this.oneTimePasswordRepository = oneTimePasswordRepository;
        this.oneTimePasswordHelpService = oneTimePasswordHelpService;
    }

    @Override
    public String returnOneTimePassword() {
        OneTimePassword oneTimePassword = new OneTimePassword();

        String otp = String.valueOf(oneTimePasswordHelpService.createRandomOneTimePassword().get());
        oneTimePassword.setOneTimePasswordCode(Integer.valueOf(otp));
        long expiryInterval = 5L * 60 * 1000;
        oneTimePassword.setExpires(new Date(System.currentTimeMillis() + expiryInterval));

        oneTimePasswordRepository.save(oneTimePassword);

        return otp;
    }

}
