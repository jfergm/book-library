package dev.fer.library.security;

import jakarta.servlet.FilterChain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;

import dev.fer.library.entity.User;
import dev.fer.library.enums.UserRole;
import dev.fer.library.service.AuthService;
import dev.fer.library.service.JwtService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthService authService;

  @Mock
  private FilterChain filterChain;

  @Mock
  CustomUserDetailsService customUserDetailsService;

  @InjectMocks
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();

    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldReturnCorrectToken() {
    String header = "Bearer thisisthetoken";
    String token = jwtAuthenticationFilter.extractToken(header);

    assertThat(token).isEqualTo("thisisthetoken");
  }

  @Test
  void shouldContinueFilterChainWhenNoAuthorizationHeader() throws Exception {
    jwtAuthenticationFilter.doFilterInternal(
      request,
      response,
      filterChain
    );

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldContinueWhenAuthorizationHeaderIsInvalid() throws Exception {
    request.addHeader("Bearer", "InvalidToken");

    jwtAuthenticationFilter.doFilterInternal(
      request,
      response,
      filterChain
    );

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldAuthenticateUserWhenTokenIsValid() throws Exception {
    String jwt = "valid-jwt-token";
    String username = "example@email.com";

    CustomUserDetails userDetails = new CustomUserDetails(new User(1L, "example@email.com", "password123", UserRole.ADMIN));

    request.addHeader("Authorization", "Bearer " + jwt);

    when(jwtService.extractSubject(jwt)).thenReturn(username);

    when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

    when(jwtService.validateToken(jwt)).thenReturn(true);

    jwtAuthenticationFilter.doFilterInternal(
      request,
      response,
      filterChain
    );

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    assertThat(username)
      .isEqualTo(SecurityContextHolder.getContext().getAuthentication().getName());

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldNotAuthenticateWhenTokenIsInvalid() throws Exception {
    String jwt = "invalid-jwt";

    request.addHeader("Authorization", "Bearer " + jwt);

    when(jwtService.validateToken(jwt))
      .thenReturn(false);

    jwtAuthenticationFilter.doFilterInternal(
      request,
      response,
      filterChain
    );

    assertNull(
      SecurityContextHolder.getContext().getAuthentication()
    );

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void shouldSkipAuthenticationWhenAlreadyAuthenticated() throws Exception {

    var existingAuth =
      mock(org.springframework.security.core.Authentication.class);

    SecurityContextHolder.getContext()
      .setAuthentication(existingAuth);

    String jwt = "token";

    request.addHeader("Authorization", "Bearer " + jwt);

    jwtAuthenticationFilter.doFilterInternal(
      request,
      response,
      filterChain
    );

    verify(customUserDetailsService, never())
      .loadUserByUsername(anyString());

    verify(filterChain).doFilter(request, response);
  }
}