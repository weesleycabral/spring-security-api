package com.wesley.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wesley.security.dto.UserLoginDTO;
import com.wesley.security.dto.UserRegistrationDTO;
import com.wesley.security.exception.InvalidCredentialsException;
import com.wesley.security.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserService userService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public void register(@RequestBody UserRegistrationDTO userDTO) {
    userService.register(userDTO);
  }

  @PostMapping("/login")
  public void login(@RequestBody UserLoginDTO userDTO) throws InvalidCredentialsException {
    userService.login(userDTO);
  }
}
