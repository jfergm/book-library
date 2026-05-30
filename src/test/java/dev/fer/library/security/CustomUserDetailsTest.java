package dev.fer.library.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dev.fer.library.entity.User;
import dev.fer.library.enums.UserRole;

class CustomUserDetailsTest {
  @Test
  void shouldConvertUserToUserDetails() {
    User user = new User(1L, "example@email.com", "password123", UserRole.ADMIN);

    CustomUserDetails userDetails = new CustomUserDetails(user);

    assertThat(userDetails.getUsername()).isEqualTo("example@email.com");
    assertTrue(
      userDetails.getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))
    );
    assertThat(userDetails.getUser().getId()).isEqualTo(user.getId());
  }
}
