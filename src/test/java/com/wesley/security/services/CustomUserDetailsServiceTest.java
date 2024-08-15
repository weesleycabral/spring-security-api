package com.wesley.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.wesley.security.entity.User;
import com.wesley.security.repository.UserRepository;
import com.wesley.security.services.Impl.CustomUserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CustomUserDetailsService customUserDetailsService = new CustomUserDetailsServiceImpl();

  private final String EMAIL = "1234@gmail.com";
  private final String PASSWORD = "1234";

  @Test
  public void shouldLoadUserByUsername() {
    User user = new User();
    user.setEmail(EMAIL);
    user.setPassword(PASSWORD);

    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

    UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);

    assertEquals(userDetails.getUsername(), EMAIL);
    assertEquals(userDetails.getPassword(), PASSWORD);
  }

  @Test
  public void shouldThrowUsernameNotFoundException() {
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> {
      customUserDetailsService.loadUserByUsername(EMAIL);
    });
  }
}