package com.wesley.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wesley.security.dto.UserLoginRequestDTO;
import com.wesley.security.dto.UserRegisterRequestDTO;
import com.wesley.security.dto.UserResponseDTO;
import com.wesley.security.exceptions.InvalidCredentialsException;
import com.wesley.security.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO userDTO) {
    return userService.register(userDTO);
  }

  @PostMapping("/login")
  public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginRequestDTO userDTO)
      throws InvalidCredentialsException {
    return userService.login(userDTO);
  }
}
