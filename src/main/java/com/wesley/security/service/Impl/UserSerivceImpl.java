package com.wesley.security.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wesley.security.dto.UserLoginDTO;
import com.wesley.security.dto.UserRegistrationDTO;
import com.wesley.security.entity.User;
import com.wesley.security.repository.UserRepository;
import com.wesley.security.service.UserService;

@Service
public class UserSerivceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public void register(UserRegistrationDTO userDTO) {
    User user = new User();
    user.setEmail(userDTO.getEmail());
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    userRepository.save(user);
  }

  @Override
  public boolean login(UserLoginDTO userDTO) {
    User user = userRepository.findByEmail(userDTO.getEmail());
    if (user != null && passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
      return true;
    }
    return false;
  }
}
