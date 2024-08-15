package com.wesley.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.auth0.jwt.algorithms.Algorithm;
import com.wesley.security.controllers.AuthController;
import com.wesley.security.dto.UserLoginRequestDTO;
import com.wesley.security.dto.UserRegisterRequestDTO;
import com.wesley.security.dto.UserResponseDTO;
import com.wesley.security.entity.User;
import com.wesley.security.exception.EmailAlreadyExistsException;
import com.wesley.security.exception.ErrorCreatingTokenException;
import com.wesley.security.exception.InvalidCredentialsException;
import com.wesley.security.exception.InvalidDataException;
import com.wesley.security.exception.UserNotFoundException;
import com.wesley.security.repository.UserRepository;
import com.wesley.security.services.Impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthController authController;

  @Mock
  private TokenService tokenService;

  @InjectMocks
  private UserService userService = new UserServiceImpl();

  private final Long ID = 1000L;
  private final String EMAIL = "1234@gmail.com";
  private final String PASSWORD = "1234";
  private final String ENCODED_PASSWORD = "encodedPassword";
  private final String TOKEN = "dummyToken";

  @Test
  public void shouldReturnAllUsers() {
    User user = new User(ID, EMAIL, null);
    when(userRepository.findAll()).thenReturn(List.of(user));

    List<User> response = userService.getAllUsers();

    assertEquals(response.size(), 1);
    assertEquals(response.get(0).getEmail(), EMAIL);
  }

  @Test
  public void shouldReturnUserById() {
    User user = new User(ID, EMAIL, null);
    when(userRepository.findById(ID)).thenReturn(Optional.ofNullable(user));

    User response = userService.getUserById(ID);

    assertEquals(response.getId(), ID);
    assertEquals(response.getEmail(), EMAIL);
  }

  @Test
  public void shouldReturnAnUser() {
    User user = new User();
    user.setEmail(EMAIL);

    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

    User response = userService.getUserByEmail(EMAIL);

    assertEquals(response.getEmail(), EMAIL);
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
  public void shouldRegisterUserSuccessfully() {
    UserRegisterRequestDTO userDTO = new UserRegisterRequestDTO(EMAIL, PASSWORD);
    User user = new User();
    user.setEmail(EMAIL);
    user.setPassword(ENCODED_PASSWORD);

    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
    when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
    when(tokenService.generateToken(any(User.class))).thenReturn(TOKEN);

    ResponseEntity<UserResponseDTO> response = userService.register(userDTO);

    assertEquals(response.getBody().email(), EMAIL);
    assertEquals(response.getBody().token(), TOKEN);

    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  public void shouldThrowEmailAlreadyExistsException() {
    UserRegisterRequestDTO userDTO = new UserRegisterRequestDTO(EMAIL, PASSWORD);
    User user = new User();
    user.setEmail(EMAIL);

    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

    assertThrows(EmailAlreadyExistsException.class, () -> {
      userService.register(userDTO);
    });

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  public void shouldThrowInvalidDataExceptionForEmptyEmail() {
    UserRegisterRequestDTO userDTO = new UserRegisterRequestDTO("", PASSWORD);
    assertThrows(InvalidDataException.class, () -> {
      userService.register(userDTO);
    });
  }

  @Test
  public void shouldThrowInvalidDataExceptionForEmptyPassword() {
    UserRegisterRequestDTO userDTO = new UserRegisterRequestDTO(EMAIL, "");
    assertThrows(InvalidDataException.class, () -> {
      userService.register(userDTO);
    });
  }

  @Test
  public void shouldLoginSuccessfully() {
    UserLoginRequestDTO loginDTO = new UserLoginRequestDTO(EMAIL, PASSWORD);
    User user = new User();
    user.setEmail(EMAIL);
    user.setPassword(ENCODED_PASSWORD);

    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
    when(tokenService.generateToken(any(User.class))).thenReturn(TOKEN);

    ResponseEntity<UserResponseDTO> response = userService.login(loginDTO);

    assertEquals(response.getBody().email(), EMAIL);
    assertEquals(response.getBody().token(), TOKEN);
  }

  @Test
  public void shouldThrowInvalidCredentialsExceptionForInvalidPassword() {
    UserLoginRequestDTO loginDTO = new UserLoginRequestDTO(EMAIL, PASSWORD);
    User user = new User();
    user.setEmail(EMAIL);
    user.setPassword(ENCODED_PASSWORD);

    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(false);

    assertThrows(InvalidCredentialsException.class, () -> {
      userService.login(loginDTO);
    });
  }

  @Test
  public void shouldThrowInvalidCredentialsExceptionForUserNotFound() {
    UserLoginRequestDTO loginDTO = new UserLoginRequestDTO(EMAIL, PASSWORD);

    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

    assertThrows(InvalidCredentialsException.class, () -> {
      userService.login(loginDTO);
    });
  }

  @Test
  public void testGenerateTokenThrowsErrorCreatingTokenException() {
    User user = new User();
    when(tokenService.generateToken(user)).thenThrow(new ErrorCreatingTokenException());
    assertThrows(ErrorCreatingTokenException.class, () -> {
      tokenService.generateToken(user);
    });
  }

}
