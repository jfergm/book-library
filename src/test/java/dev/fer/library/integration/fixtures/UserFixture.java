package dev.fer.library.integration.fixtures;

import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import dev.fer.library.dto.request.LoginRequest;
import dev.fer.library.dto.response.LoginResponse;

public class UserFixture {

  private final TestRestTemplate restTemplate;

  public UserFixture(TestRestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public LoginResponse getLoginResponse() {
    LoginRequest request = new LoginRequest("email@email.com", "password123");
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);
    
    return response.getBody();
  }
}