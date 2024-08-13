package com.wesley.security.services;

import java.util.List;

import com.wesley.security.dto.UserLoginDTO;
import com.wesley.security.dto.UserRegistrationDTO;
import com.wesley.security.dto.UserResponseDTO;
import com.wesley.security.exception.UserNotFoundException;

public interface UserService {
  public List<UserResponseDTO> getAllUsers();

  public UserResponseDTO getUserById(Long id) throws UserNotFoundException;

  public UserResponseDTO getUserByEmail(String email) throws UserNotFoundException;

  public void register(UserRegistrationDTO userDTO);

  public void login(UserLoginDTO userDTO);
}
