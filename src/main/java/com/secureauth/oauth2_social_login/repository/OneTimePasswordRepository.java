package com.secureauth.oauth2_social_login.repository;

import com.secureauth.oauth2_social_login.entity.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long> {
    Optional<OneTimePassword> findByOneTimePasswordCode(String oneTimePasswordCode);
}
