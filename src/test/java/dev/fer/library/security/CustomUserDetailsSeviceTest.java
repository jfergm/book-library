package dev.fer.library.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import dev.fer.library.entity.User;
import dev.fer.library.enums.UserRole;
import dev.fer.library.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsSeviceTest {
  @InjectMocks
  CustomUserDetailsService userDetailsService;

  @Mock
  UserRepository userRepository;

  @Test
  void shouldLoadUserByEmail() {
    when(userRepository.findByEmail(anyString())).thenReturn(
      Optional.of(new User(1L, "example@email.com", "password123", UserRole.ADMIN))
    );

    UserDetails userDetails = userDetailsService.loadUserByUsername("example@email.com");

    assertThat(userDetails.getUsername()).isEqualTo("example@email.com");
    assertThat(userDetails.getPassword()).isEqualTo("password123");
    assertThat(userDetails.getAuthorities().size()).isEqualTo(1);
    
    verify(userRepository).findByEmail(anyString());
  }

  @Test
  void shouldThrowWhenUserNotExist() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    assertThrows(
      UsernameNotFoundException.class, 
      () -> userDetailsService.loadUserByUsername("example@email.com")
    );

    verify(userRepository).findByEmail(anyString());
  }
}
