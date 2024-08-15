package com.wesley.security.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.wesley.security.dto.UserLoginRequestDTO;
import com.wesley.security.dto.UserRegisterRequestDTO;
import com.wesley.security.dto.UserResponseDTO;
import com.wesley.security.entity.User;
import com.wesley.security.exception.EmailAlreadyExistsException;
import com.wesley.security.exception.InvalidCredentialsException;
import com.wesley.security.exception.InvalidDataException;
import com.wesley.security.exception.UserNotFoundException;
import com.wesley.security.repository.UserRepository;
import com.wesley.security.services.TokenService;
import com.wesley.security.services.UserService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TokenService tokenService;

  @Override
  public List<User> getAllUsers() {
    List<User> users = userRepository.findAll();
    return users;
  }

  @Override
  public User getUserById(Long id) throws UserNotFoundException {
    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

    return user;
  }

  @Override
  public User getUserByEmail(String email) throws UserNotFoundException {
    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

    return user;
  }

  @Override
  public ResponseEntity<UserResponseDTO> register(UserRegisterRequestDTO userDTO) {
    Optional<User> existingUser = userRepository.findByEmail(userDTO.email());
    if (existingUser.isPresent()) {
      throw new EmailAlreadyExistsException();
    }

    if (userDTO.email().isEmpty() || userDTO.password().isEmpty()) {
      throw new InvalidDataException();
    }

    User user = new User();
    user.setEmail(userDTO.email());
    user.setPassword(passwordEncoder.encode(userDTO.password()));
    userRepository.save(user);

    String token = this.tokenService.generateToken(user);
    UserResponseDTO responseDTO = new UserResponseDTO(user.getEmail(), token);

    return ResponseEntity.ok(responseDTO);
  }

  @Override
  public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginRequestDTO userLoginDTO)
      throws InvalidCredentialsException {
    Optional<User> optionalUser = userRepository.findByEmail(userLoginDTO.email());
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (passwordEncoder.matches(userLoginDTO.password(), user.getPassword())) {
        String token = this.tokenService.generateToken(user);
        return ResponseEntity.ok(new UserResponseDTO(user.getEmail(), token));
      }
    }
    throw new InvalidCredentialsException();
  }
}
