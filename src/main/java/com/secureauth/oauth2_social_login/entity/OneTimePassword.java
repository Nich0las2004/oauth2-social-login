package com.secureauth.oauth2_social_login.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "otps")
public class OneTimePassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;
    @NonNull
    private String oneTimePasswordCode;
    @NonNull
    private Date expires;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOneTimePasswordCode() {
        return oneTimePasswordCode;
    }
    public void setOneTimePasswordCode(String oneTimePasswordCode) {
        this.oneTimePasswordCode = oneTimePasswordCode;
    }
    public Date getExpires() {
        return expires;
    }
    public void setExpires(Date expires) {
        this.expires = expires;
    }

}
