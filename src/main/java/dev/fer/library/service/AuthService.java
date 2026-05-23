package dev.fer.library.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.fer.library.dto.request.UserRequest;
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

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
  }

  public UserResponse register(UserRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new BadRequestException("Email already exists");
    }

    User user = new User(null, request.email(), passwordEncoder.encode(request.password()), request.role());
    
    return userMapper.toResponse(userRepository.save(user)); 
  }
}
