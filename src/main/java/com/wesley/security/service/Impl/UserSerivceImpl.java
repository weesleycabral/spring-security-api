package com.wesley.security.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wesley.security.dto.UserLoginDTO;
import com.wesley.security.dto.UserRegistrationDTO;
import com.wesley.security.entity.User;
import com.wesley.security.exception.UserNotFoundException;
import com.wesley.security.repository.UserRepository;
import com.wesley.security.service.UserService;

@Service
public class UserSerivceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public List<UserRegistrationDTO> getAllUsers() {
    List<User> users = userRepository.findAll();
    return UserRegistrationDTO.toDTOs(users);
  }

  @Override
  public UserRegistrationDTO getUserById(Long id) throws UserNotFoundException {
    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

    return UserRegistrationDTO.toDTO(user);
  }

  @Override
  public UserRegistrationDTO getUserByEmail(String email) throws UserNotFoundException {
    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

    return UserRegistrationDTO.toDTO(user);
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
    return userRepository.findByEmail(userDTO.getEmail())
        .map(user -> passwordEncoder.matches(userDTO.getPassword(), user.getPassword()))
        .orElse(false);
  }
}
