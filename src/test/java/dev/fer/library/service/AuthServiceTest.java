package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.fer.library.dto.request.LoginRequest;
import dev.fer.library.dto.request.UserRequest;
import dev.fer.library.dto.response.LoginResponse;
import dev.fer.library.dto.response.UserResponse;
import dev.fer.library.entity.User;
import dev.fer.library.enums.UserRole;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.mapper.UserMapper;
import dev.fer.library.repository.UserRepository;
import dev.fer.library.security.CustomUserDetails;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
  AuthService authService;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  UserRepository userRepository;

  @Mock
  JwtService jwtService;

  @Mock
  AuthenticationManager authenticationManager;

  UserMapper userMapper = new UserMapper();

  @BeforeEach
  void setUp() {
    authService = new AuthService(
      userRepository, 
      passwordEncoder, 
      userMapper, 
      jwtService, 
      authenticationManager
    );
  }


  @Test
  void shouldRegisterUser() {
    when(passwordEncoder.encode("password123")).thenReturn("encodedpassword");
    when(userRepository.existsByEmail("email@example.com")).thenReturn(false);
    when(userRepository.save(any())).thenReturn(
      new User(
        1L,
        "email@example.com",
        "encodedpassword",
        UserRole.ADMIN
      )
    );
    UserRequest request = new UserRequest("email@example.com", "password123", UserRole.ADMIN);

    UserResponse created = authService.register(request);

    assertThat(created.email()).isEqualTo(request.email());
    assertThat(created.role()).isEqualTo(request.role());

    verify(userRepository).existsByEmail("email@example.com");
    verify(passwordEncoder).encode("password123");
    verify(userRepository).save(any(User.class));
  }

  @Test
  void shouldThrowWhenRegisterUserWithExistingEmail() {
    when(userRepository.existsByEmail("email@example.com")).thenReturn(true);
    UserRequest request = new UserRequest("email@example.com", "password123", UserRole.ADMIN);

    assertThrows(BadRequestException.class, () -> authService.register(request));

    verify(userRepository).existsByEmail("email@example.com");
    verify(passwordEncoder, times(0)).encode("password123");
    verify(userRepository, times(0)).save(any(User.class));
  }

  @Test
  void shouldReturnAuthToken() {
    User user = new User(1L, "email@example.com", "password123", UserRole.USER);
    CustomUserDetails userDetails = new CustomUserDetails(user);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
      userDetails, null, userDetails.getAuthorities()
    );

    when(authenticationManager.authenticate(any())).thenReturn(authentication);

    when(jwtService.generateToken(any())).thenReturn("json.web.token");
    LoginRequest request = new LoginRequest("example@email.com", "password123");
    LoginResponse login = authService.login(request);

    assertThat(login.token()).isEqualTo("json.web.token");
    verify(jwtService).generateToken(any());
  }

  @Test
  void shouldThrowWhenInvalidCredentials() {
    when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

    LoginRequest request = new LoginRequest("example@email.com", "password123");
    
    assertThrows(BadCredentialsException.class, () -> authService.login(request));

    verify(jwtService,times(0)).generateToken(any());
  }

}
