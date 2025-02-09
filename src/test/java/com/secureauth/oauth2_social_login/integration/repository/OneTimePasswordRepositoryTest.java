package com.secureauth.oauth2_social_login.integration.repository;

import com.secureauth.oauth2_social_login.entity.OneTimePassword;
import com.secureauth.oauth2_social_login.repository.OneTimePasswordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OneTimePasswordRepositoryTest {

    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    @Test
    public void testFindByOneTimePasswordCode() {
        OneTimePassword otp = new OneTimePassword();
        otp.setOneTimePasswordCode("123456");
        oneTimePasswordRepository.save(otp);

        Optional<OneTimePassword> foundOtp = oneTimePasswordRepository.findByOneTimePasswordCode("123456");

        assertTrue(foundOtp.isPresent());
        assertEquals("123456", foundOtp.get().getOneTimePasswordCode());
    }
}