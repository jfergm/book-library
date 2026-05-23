package dev.fer.library.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import dev.fer.library.dto.response.UserResponse;
import dev.fer.library.entity.User;
import dev.fer.library.enums.UserRole;

public class UserMapperTest {
  UserMapper userMapper = new UserMapper();

  @Test
  void shouldConvertToUserResponse() {
    User user = new User(1L, "email@email.com", "encoded", UserRole.ADMIN);
    
    UserResponse response = userMapper.toResponse(user);

    assertThat(response.email()).isEqualTo(user.getEmail());
    assertThat(response.role()).isEqualTo(user.getRole());
  }
}
