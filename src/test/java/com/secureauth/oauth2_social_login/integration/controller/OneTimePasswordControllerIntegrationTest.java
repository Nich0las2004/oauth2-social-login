package com.secureauth.oauth2_social_login.integration.controller;

import com.secureauth.oauth2_social_login.controller.OneTimePasswordController;
import com.secureauth.oauth2_social_login.entity.User;
import com.secureauth.oauth2_social_login.repository.UserRepository;
import com.secureauth.oauth2_social_login.service.notification.NotificationService;
import com.secureauth.oauth2_social_login.service.otp.OneTimePasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(OneTimePasswordController.class)
public class OneTimePasswordControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OneTimePasswordService oneTimePasswordService;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "testuser")
    public void testCreateOTPForm() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(oneTimePasswordService.returnOneTimePassword()).thenReturn("123456");

        mockMvc.perform(get("/otp/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("otp"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "OTP sent successfully to testuser@example.com"));

        verify(notificationService).notifyUser("testuser@example.com", "Your One-Time Password (OTP) is: 123456");
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testValidateOTP() throws Exception {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "testuser",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(oneTimePasswordService.validateOneTimePassword("123456")).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.getAuthorities()));

        mockMvc.perform(post("/otp/validate").param("otp", "123456").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/dashboard"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testValidateInvalidOTP() throws Exception {
        when(oneTimePasswordService.validateOneTimePassword("invalid")).thenReturn(false);

        mockMvc.perform(post("/otp/validate").param("otp", "invalid").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("otp"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "OTP is invalid"));
    }
}