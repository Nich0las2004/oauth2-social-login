package com.secureauth.oauth2_social_login.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class OneTimePassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;
    @NonNull
    private Integer oneTimePasswordCode;
    @NonNull
    private Date expires;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getOneTimePasswordCode() {
        return oneTimePasswordCode;
    }
    public void setOneTimePasswordCode(Integer oneTimePasswordCode) {
        this.oneTimePasswordCode = oneTimePasswordCode;
    }
    public Date getExpires() {
        return expires;
    }
    public void setExpires(Date expires) {
        this.expires = expires;
    }

}
