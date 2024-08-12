package com.wesley.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wesley.security.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

}
