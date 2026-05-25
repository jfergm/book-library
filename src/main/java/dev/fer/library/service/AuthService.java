package dev.fer.library.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.LoginRequest;
import dev.fer.library.dto.request.UserRequest;
import dev.fer.library.dto.response.LoginResponse;
import dev.fer.library.dto.response.UserResponse;
import dev.fer.library.entity.User;
import dev.fer.library.exception.BadRequestException;
import dev.fer.library.mapper.UserMapper;
import dev.fer.library.repository.UserRepository;

@Service
public class AuthService {
  private UserRepository userRepository;

  private PasswordEncoder passwordEncoder;

  private UserMapper userMapper;

  private JwtService jwtService;

  private AuthenticationManager authenticationManager;

  public AuthService(
    UserRepository userRepository, 
    PasswordEncoder passwordEncoder, 
    UserMapper userMapper,
    JwtService jwtService,
    AuthenticationManager authenticationManager
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  public UserResponse register(UserRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new BadRequestException("Email already exists");
    }

    User user = new User(null, request.email(), passwordEncoder.encode(request.password()), request.role());
    
    return userMapper.toResponse(userRepository.save(user)); 
  }

  public LoginResponse login(LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.email(), request.password())
    );

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String token = jwtService.generateToken(userDetails);
    LoginResponse login = new LoginResponse(token);
    return login;
  }
}
