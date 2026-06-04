package dev.fer.library.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import dev.fer.library.dto.request.LoginRequest;
import dev.fer.library.dto.request.UserRequest;
import dev.fer.library.dto.response.LibraryResponse;
import dev.fer.library.dto.response.LoginResponse;
import dev.fer.library.enums.UserRole;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@AutoConfigureTestRestTemplate
@Sql("/data.sql")
class AuthenticationIT {
  @Autowired
  TestRestTemplate restTemplate;
  
  @Container
  @ServiceConnection
  static PostgreSQLContainer postgres = new PostgreSQLContainer(
    DockerImageName.parse("postgres:alpine3.22")
  );
    
  @Test
  void shouldRegisterAndLogin() {
    UserRequest request = new UserRequest("example@email.com", "password123", UserRole.USER);
    restTemplate.postForLocation("/auth/register", request);
    
    ResponseEntity<Void> forbiddenResponse = restTemplate.getForEntity("/libraries", Void.class);
    assertThat(forbiddenResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    
    LoginRequest loginRequest = new LoginRequest(request.email(), request.password());
    LoginResponse loginResponse = restTemplate.postForObject("/auth/login", loginRequest, LoginResponse.class);
    assertThat(loginResponse.token()).isNotBlank();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + loginResponse.token());
    HttpEntity<String> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<LibraryResponse[]> okResponse = restTemplate.exchange(
      "/libraries",
      HttpMethod.GET,
      httpEntity,
      LibraryResponse[].class);

    assertThat(okResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void shouldFail() {
    LoginRequest failRequest = new LoginRequest("test@email.com", "badpassword");
    ResponseEntity<LoginResponse> failResponse = restTemplate.postForEntity("/auth/login", failRequest, LoginResponse.class);
    assertThat(failResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    LoginRequest successRequest = new LoginRequest("test@email.com", "password123");
    ResponseEntity<LoginResponse> successResponse = restTemplate.postForEntity("/auth/login", successRequest, LoginResponse.class);
    assertThat(successResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
