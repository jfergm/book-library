package dev.fer.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.dto.request.LoginRequest;
import dev.fer.library.dto.request.UserRequest;
import dev.fer.library.dto.response.LoginResponse;
import dev.fer.library.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private AuthService authService;

  protected AuthController(AuthService authService) {
    this.authService = authService;
  }
  
  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid UserRequest request) {
    authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest login) {
    LoginResponse response = authService.login(login);
    return ResponseEntity.ok(response);
  }
  
}
