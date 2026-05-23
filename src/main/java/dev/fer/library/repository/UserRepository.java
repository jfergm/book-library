package dev.fer.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.fer.library.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Boolean existsByEmail(String email);
}