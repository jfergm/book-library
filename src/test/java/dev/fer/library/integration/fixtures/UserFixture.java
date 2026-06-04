package dev.fer.library.integration.fixtures;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dev.fer.library.dto.request.LoginRequest;
import dev.fer.library.dto.response.LoginResponse;

public class UserFixture {

  private final TestRestTemplate restTemplate;

  public UserFixture(TestRestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public LoginResponse getLoginResponse() {
    LoginRequest request = new LoginRequest("test@email.com", "password123");
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    return response.getBody();
  }
}