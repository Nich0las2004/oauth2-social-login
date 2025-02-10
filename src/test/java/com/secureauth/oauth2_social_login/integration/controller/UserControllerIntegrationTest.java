package com.secureauth.oauth2_social_login.integration.controller;

import com.secureauth.oauth2_social_login.controller.UserController;
import com.secureauth.oauth2_social_login.dto.UserDTO;
import com.secureauth.oauth2_social_login.entity.User;
import com.secureauth.oauth2_social_login.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @WithMockUser
    public void testShowRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser
    public void testRegister() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("testuser@example.com");
        userDTO.setPassword("password");

        when(bCryptPasswordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

        mockMvc.perform(post("/register")
                        .flashAttr("user", userDTO)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register-success"));

        verify(userService).registerUser(any(User.class));
    }
}