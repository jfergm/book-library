package dev.fer.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.fer.library.dto.request.LoginRequest;
import dev.fer.library.dto.request.UserRequest;
import dev.fer.library.dto.response.LoginResponse;
import dev.fer.library.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication", description = "Authentication endpoints")
@RestController
@RequestMapping("/auth")
public class AuthController {

  private AuthService authService;

  protected AuthController(AuthService authService) {
    this.authService = authService;
  }
  
  @Operation(summary = "Register user", description = "Register a new user")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Register successful"),
    @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
  })
  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid UserRequest request) {
    authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "Login", description = "Login with credentials")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Login successful"),
    @ApiResponse(responseCode = "403", description = "Bad credentials", content = @Content)
  })
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest login) {
    LoginResponse response = authService.login(login);
    return ResponseEntity.ok(response);
  }
  
}
