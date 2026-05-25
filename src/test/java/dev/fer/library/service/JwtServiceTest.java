package dev.fer.library.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import dev.fer.library.entity.User;
import dev.fer.library.enums.UserRole;
import dev.fer.library.security.CustomUserDetails;

public class JwtServiceTest {
  JwtService jwtService = new JwtService("SecretjwttestSecretjwttestSecretjwttest123456", 86400000L);

  private CustomUserDetails user = 
    new CustomUserDetails(new User(1L, "example@email.com", "password", UserRole.ADMIN));

  @Test
  void shouldGenerateValidToken() {
    String token = jwtService.generateToken(user);

    assertThat(token).isNotEmpty();

    assertThat(jwtService.extractSubject(token)).isEqualTo("example@email.com");
  }

  @Test
  void shouldVerifyToken() {
    String token = jwtService.generateToken(user);
    boolean isValid = jwtService.validateToken(token);

    assertThat(isValid).isTrue();
  }

  @Test
  void shouldVerifyTokenAndReturnFalse() {
    boolean isValid = jwtService.validateToken("invalid.token");

    assertThat(isValid).isFalse();
  }

  @Test
  void shouldVerifyTokenAndReturnFalseWhenExipred() {
    JwtService anotherService = 
      new JwtService("SecretjwttestSecretjwttestSecretjwttest123456", -86400000L);
    
    String token = anotherService.generateToken(user);

    assertThat(anotherService.validateToken(token)).isFalse();
  }
}
