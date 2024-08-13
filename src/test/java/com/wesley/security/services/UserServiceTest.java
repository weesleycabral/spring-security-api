package com.wesley.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wesley.security.controller.AuthController;
import com.wesley.security.dto.UserLoginDTO;
import com.wesley.security.dto.UserRegistrationDTO;
import com.wesley.security.dto.UserResponseDTO;
import com.wesley.security.entity.User;
import com.wesley.security.exception.EmailAlreadyExistsException;
import com.wesley.security.exception.InvalidCredentialsException;
import com.wesley.security.exception.InvalidDataException;
import com.wesley.security.exception.UserNotFoundException;
import com.wesley.security.repository.UserRepository;
import com.wesley.security.services.Impl.UserSerivceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthController authController;

  @InjectMocks
  private UserService userService = new UserSerivceImpl();

  private final Long ID = 1000L;
  private final String EMAIL = "1234@gmail.com";
  private final String PASSWORD = "1234";
  private final String ENCODED_PASSWORD = "encodedPassword";

  @Test
  public void shouldReturnAllUsers() {
    User user = new User(ID, EMAIL, null);
    when(userRepository.findAll()).thenReturn(List.of(user));

    List<UserResponseDTO> response = userService.getAllUsers();

    assertEquals(response.size(), 1);
    assertEquals(response.get(0).getId(), ID);
    assertEquals(response.get(0).getEmail(), EMAIL);
  }

  @Test
  public void shouldReturnUserById() {
    User user = new User(ID, EMAIL, null);
    when(userRepository.findById(ID)).thenReturn(Optional.ofNullable(user));

    UserResponseDTO response = userService.getUserById(ID);

    assertEquals(response.getId(), ID);
    assertEquals(response.getEmail(), EMAIL);
  }

  @Test
  public void shouldReturnAnUser() {
    User user = new User();
    user.setEmail(EMAIL);

    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

    UserResponseDTO response = userService.getUserByEmail(EMAIL);

    assertEquals(response.getEmail(), EMAIL);
  }

  @Test
  public void shouldRegisterAnUserSuccessfully() {
    UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
    userRegistrationDTO.setEmail(EMAIL);
    userRegistrationDTO.setPassword(PASSWORD);

    User user = new User();
    user.setEmail(EMAIL);
    user.setPassword(ENCODED_PASSWORD);

    when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
    when(userRepository.save(user)).thenReturn(user);

    userService.register(userRegistrationDTO);

    verify(passwordEncoder).encode(PASSWORD);
    verify(userRepository).save(user);
  }

  @Test
  void shouldThrowWhenUserNotFoundById() {
    when(userRepository.findById(ID)).thenReturn(Optional.ofNullable(null));

    assertThrows(UserNotFoundException.class, () -> {
      userService.getUserById(ID);
    });
  }

  @Test
  void shouldThrowWhenUserNotFoundByEmail() {
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(null));

    assertThrows(UserNotFoundException.class, () -> {
      userService.getUserByEmail(EMAIL);
    });
  }

  @Test
  public void shouldThrowLoginInvalidCredentials() {
    UserLoginDTO userLoginDTO = new UserLoginDTO();
    userLoginDTO.setEmail("1234@gmail.com");
    userLoginDTO.setPassword("wrongpassword");

    User user = new User();
    user.setEmail("1234@gmail.com");
    user.setPassword("encodedpassword");

    when(userRepository.findByEmail(userLoginDTO.getEmail())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())).thenReturn(false);

    assertThrows(InvalidCredentialsException.class, () -> {
      userService.login(userLoginDTO);
    });
  }

  @Test
  public void shouldThrowExceptionWhenEmailAlreadyExists() {
    UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
    userRegistrationDTO.setEmail(EMAIL);
    userRegistrationDTO.setPassword(PASSWORD);

    User existingUser = new User();
    existingUser.setEmail(EMAIL);

    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(existingUser));

    assertThrows(EmailAlreadyExistsException.class, () -> {
      userService.register(userRegistrationDTO);
    });
  }

  @Test
  public void shouldThrowExceptionWhenInvalidData() {
    UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();
    userRegistrationDTO.setEmail("");
    userRegistrationDTO.setPassword("");

    assertThrows(InvalidDataException.class, () -> {
      userService.register(userRegistrationDTO);
    });
  }

}
