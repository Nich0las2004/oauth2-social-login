package com.secureauth.oauth2_social_login.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    fields for regular registration
    private String username;
    private String email;
    private String password;

//    fields for gitHub
    private int githubUserId;

//    fields for facebook
    private String facebookUserId;

//    fields for both facebook and GitHub
    private String name;
    private String provider;


    public User() {
    }

    public User(String name, String provider, int githubUserId, String email) {
        this.name = name;
        this.provider = provider;
        this.githubUserId = githubUserId;
        this.email = email;
    }

    public User(String name, String provider, String facebookUserId, String email) {
        this.name = name;
        this.provider = provider;
        this.facebookUserId = facebookUserId;
        this.email = email;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getGithubUserId() {
        return githubUserId;
    }

    public void setGithubUserId(int githubUserId) {
        this.githubUserId = githubUserId;
    }

    public String getFacebookUserId() {
        return facebookUserId;
    }

    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
