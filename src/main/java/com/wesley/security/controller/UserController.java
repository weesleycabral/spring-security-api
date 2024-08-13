package com.wesley.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wesley.security.dto.UserResponseDTO;
import com.wesley.security.exception.UserNotFoundException;
import com.wesley.security.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  @ResponseStatus(value = HttpStatus.OK)
  public List<UserResponseDTO> getAll() {
    return userService.getAllUsers();
  }

  @GetMapping("/id/{id}")
  @ResponseStatus(value = HttpStatus.OK)
  public UserResponseDTO getById(@PathVariable("id") Long id) throws UserNotFoundException {
    return userService.getUserById(id);
  }

  @GetMapping("/email/{email}")
  @ResponseStatus(value = HttpStatus.OK)
  public UserResponseDTO getByEmail(@Valid @Email @PathVariable("email") String email)
      throws UserNotFoundException {
    return userService.getUserByEmail(email);
  }
}
