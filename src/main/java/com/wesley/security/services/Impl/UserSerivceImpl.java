package com.wesley.security.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wesley.security.dto.UserLoginDTO;
import com.wesley.security.dto.UserRegistrationDTO;
import com.wesley.security.dto.UserResponseDTO;
import com.wesley.security.entity.User;
import com.wesley.security.exception.EmailAlreadyExistsException;
import com.wesley.security.exception.InvalidCredentialsException;
import com.wesley.security.exception.InvalidDataException;
import com.wesley.security.exception.UserNotFoundException;
import com.wesley.security.repository.UserRepository;
import com.wesley.security.services.UserService;

import jakarta.validation.Valid;

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
  public void register(@Valid UserRegistrationDTO userDTO) {
    Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
    if (existingUser.isPresent()) {
      throw new EmailAlreadyExistsException();
    }

    if (userDTO.getEmail().isEmpty() || userDTO.getPassword().isEmpty()) {
      throw new InvalidDataException();
    }
    User user = new User();
    user.setEmail(userDTO.getEmail());
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    userRepository.save(user);
  }

  @Override
  public void login(UserLoginDTO userLoginDTO) throws InvalidCredentialsException {
    Optional<User> optionalUser = userRepository.findByEmail(userLoginDTO.getEmail());
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
        throw new InvalidCredentialsException();
      }
    } else {
      throw new InvalidCredentialsException();
    }
  }
}
