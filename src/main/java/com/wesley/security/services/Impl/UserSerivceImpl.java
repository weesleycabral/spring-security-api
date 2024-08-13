package com.wesley.security.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wesley.security.dto.UserLoginDTO;
import com.wesley.security.dto.UserRegistrationDTO;
import com.wesley.security.dto.UserResponseDTO;
import com.wesley.security.entity.User;
import com.wesley.security.exception.UserNotFoundException;
import com.wesley.security.repository.UserRepository;
import com.wesley.security.services.UserService;

@Service
public class UserSerivceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public List<UserResponseDTO> getAllUsers() {
    List<User> users = userRepository.findAll();
    return UserResponseDTO.toResponseDTOs(users);
  }

  @Override
  public UserResponseDTO getUserById(Long id) throws UserNotFoundException {
    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

    return UserResponseDTO.toResponseDTO(user);
  }

  @Override
  public UserResponseDTO getUserByEmail(String email) throws UserNotFoundException {
    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

    return UserResponseDTO.toResponseDTO(user);
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
