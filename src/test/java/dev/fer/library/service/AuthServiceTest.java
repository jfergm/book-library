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
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.fer.library.dto.request.UserRequest;
import dev.fer.library.dto.response.UserResponse;
import dev.fer.library.entity.User;
import dev.fer.library.enums.UserRole;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.mapper.UserMapper;
import dev.fer.library.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
  AuthService authService;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  UserRepository userRepository;

  UserMapper userMapper = new UserMapper();

  @BeforeEach
  void setUp() {
    authService = new AuthService(userRepository, passwordEncoder, userMapper);
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
}
