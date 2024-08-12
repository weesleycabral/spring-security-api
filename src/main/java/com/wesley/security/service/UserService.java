package com.wesley.security.service;

import java.util.List;

import com.wesley.security.dto.UserLoginDTO;
import com.wesley.security.dto.UserRegistrationDTO;
import com.wesley.security.exception.UserNotFoundException;

public interface UserService {
  public List<UserRegistrationDTO> getAllUsers();

  public UserRegistrationDTO getUserById(Long id) throws UserNotFoundException;

  public UserRegistrationDTO getUserByEmail(String email) throws UserNotFoundException;

  public void register(UserRegistrationDTO userDTO);

  public boolean login(UserLoginDTO userDTO);
}
