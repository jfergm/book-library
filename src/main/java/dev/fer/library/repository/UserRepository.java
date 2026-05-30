package dev.fer.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.fer.library.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
}