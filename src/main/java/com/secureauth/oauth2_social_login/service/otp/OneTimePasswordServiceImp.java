package com.secureauth.oauth2_social_login.service.otp;

import com.secureauth.oauth2_social_login.entity.OneTimePassword;
import com.secureauth.oauth2_social_login.repository.OneTimePasswordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OneTimePasswordServiceImp implements OneTimePasswordService{
    private static final Logger logger = LoggerFactory.getLogger(OneTimePasswordServiceImp.class);
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
        oneTimePassword.setOneTimePasswordCode(otp);
        long expiryInterval = 5L * 60 * 1000;
        oneTimePassword.setExpires(new Date(System.currentTimeMillis() + expiryInterval));

        oneTimePasswordRepository.save(oneTimePassword);

        return otp;
    }

    @Override
    public boolean validateOneTimePassword(String otp) {
        Optional<OneTimePassword> storedOtp = oneTimePasswordRepository.findByOneTimePasswordCode(otp);

        if (storedOtp.isEmpty()) {
            logger.warn("Failed OTP validation attempt for OTP: {}", otp);
            return false;
        }

        logger.info("OTP successfully validated.");

        OneTimePassword otpEntity = storedOtp.get();

        return !otpEntity.getExpires().before(new Date());
    }

}
