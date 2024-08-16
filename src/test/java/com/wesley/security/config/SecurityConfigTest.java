package com.wesley.security.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wesley.security.infra.SecurityFilter;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

  @Mock
  private SecurityFilter securityFilter;

  @Mock
  private HttpSecurity httpSecurity;

  @Mock
  private AuthenticationConfiguration authenticationConfiguration;

  @InjectMocks
  private SecurityConfig securityConfig;

  @Test
  public void testPasswordEncoder() {
    PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
    assertNotNull(passwordEncoder);
    assert (passwordEncoder instanceof BCryptPasswordEncoder);
  }
}