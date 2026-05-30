package dev.fer.library.mapper;

import org.springframework.stereotype.Component;

import dev.fer.library.dto.response.UserResponse;
import dev.fer.library.entity.User;

@Component
public class UserMapper {

  public UserResponse toResponse(User user) {
    return new UserResponse(user.getEmail(), user.getRole());
  }
}
