package com.secureauth.oauth2_social_login.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2SuccessHandler successHandler) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(HeadersConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/register", "/register-success").permitAll()
                        .requestMatchers("/otp/create").permitAll()
                        .requestMatchers("/notifications").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .defaultSuccessUrl("/dashboard", true))
                .oauth2Login(oauth2 -> oauth2.successHandler(successHandler))
        ;

        return http.build();
    }
}
