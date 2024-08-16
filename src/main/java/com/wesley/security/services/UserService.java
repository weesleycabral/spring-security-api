package com.wesley.security.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.wesley.security.dto.UserLoginRequestDTO;
import com.wesley.security.dto.UserRegisterRequestDTO;
import com.wesley.security.dto.UserResponseDTO;
import com.wesley.security.entity.User;
import com.wesley.security.exceptions.UserNotFoundException;

public interface UserService {
  public List<User> getAllUsers();

  public User getUserById(Long id) throws UserNotFoundException;

  public User getUserByEmail(String email) throws UserNotFoundException;

  public ResponseEntity<UserResponseDTO> register(UserRegisterRequestDTO userDTO);

  public ResponseEntity<UserResponseDTO> login(UserLoginRequestDTO userDTO);
}
