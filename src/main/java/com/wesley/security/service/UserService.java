package com.wesley.security.service;

import java.util.List;

import com.wesley.security.dto.UserLoginDTO;
import com.wesley.security.dto.UserRegistrationDTO;
import com.wesley.security.entity.User;

public interface UserService {
  public List<User> getAllUsers();

  public void register(UserRegistrationDTO userDTO);

  public boolean login(UserLoginDTO userDTO);
}
