package dev.fer.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.fer.library.dto.request.LoginRequest;
import dev.fer.library.dto.request.UserRequest;
import dev.fer.library.dto.response.LoginResponse;
import dev.fer.library.dto.response.UserResponse;
import dev.fer.library.enums.UserRole;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.security.CustomUserDetailsService;
import dev.fer.library.security.SecurityConfig;
import dev.fer.library.service.AuthService;
import dev.fer.library.service.JwtService;
import dev.fer.library.utils.TestUtils;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {
  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  AuthService authService;

  @MockitoBean
  CustomUserDetailsService customUserDetailsService;

  @MockitoBean
  JwtService jwtService;

  @Test
  void shouldRegisterUser() throws Exception {
    when(authService.register(any())).thenReturn(
      new UserResponse("email@example.com", UserRole.ADMIN)
    );
    UserRequest request = 
      new UserRequest("jose.gomezm12@gmail.com", "password123", UserRole.ADMIN);
    
    mockMvc
      .perform(
        post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isCreated());
    
    verify(authService).register(any());
  }

  @Test
  void shouldThrowRegisterUserWhenInvalidEmail() throws Exception {
    when(authService.register(any())).thenThrow(BadRequestException.class);
    UserRequest request = 
      new UserRequest("jose.gomezm12@gmail.com", "password123", UserRole.ADMIN);
    
    mockMvc
      .perform(
        post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isBadRequest());
    
    verify(authService).register(any());
  }

  @Test
  void shouldReturnTokenWhenLogin() throws Exception {
    when(authService.login(any())).thenReturn(
      new LoginResponse("json.web.token", 1L, "email@example.com")
    );

    LoginRequest request = new LoginRequest("email@example.com", "password");
    
    mockMvc
      .perform(
        post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectAsJson(request)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token").value("json.web.token"))
      .andExpect(jsonPath("$.id").value("1"))
      .andExpect(jsonPath("$.email").value("email@example.com"));
    
    verify(authService).login(any());
  }
}
