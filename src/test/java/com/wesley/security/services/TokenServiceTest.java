package com.wesley.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.wesley.security.entity.User;
import com.wesley.security.services.Impl.TokenServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

  @InjectMocks
  private TokenServiceImpl tokenService = new TokenServiceImpl();

  @Mock
  private User user;

  private final String SECRET = "testSecret";
  private final String EMAIL = "test@example.com";
  private final String APP_NAME = "spring-security-app";

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(tokenService, "secret", SECRET);
  }

  @Test
  public void shouldGenerateExpirationDate() {
    Instant expirationDate = tokenService.generateExpirationDate();
    Instant expectedDate = LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    assertEquals(expectedDate.getEpochSecond(), expirationDate.getEpochSecond(), 1);
  }

  @Test
  public void shouldGenerateTokenSuccessfully() {
    when(user.getEmail()).thenReturn(EMAIL);
    String token = tokenService.generateToken(user);
    String subject = JWT.require(Algorithm.HMAC256(SECRET)).withIssuer(APP_NAME).build().verify(token)
        .getSubject();
    assertEquals(EMAIL, subject);
  }

  @Test
  public void shouldThrowErrorCreatingTokenException() {
    ReflectionTestUtils.setField(tokenService, "secret", "");
    when(user.getEmail()).thenReturn(EMAIL);
    assertThrows(RuntimeException.class, () -> {
      tokenService.generateToken(user);
    });
  }

  @Test
  public void shouldValidateTokenSuccessfully() {
    String token = JWT.create().withIssuer(APP_NAME).withSubject(EMAIL)
        .withExpiresAt(tokenService.generateExpirationDate()).sign(Algorithm.HMAC256(SECRET));
    String subject = tokenService.validateToken(token);
    assertEquals(EMAIL, subject);
  }

  @Test
  public void shouldReturnNullForInvalidToken() {
    String invalidToken = "invalidToken";
    assertNull(tokenService.validateToken(invalidToken));
  }

  @Test
  public void shouldReturnNullForExpiredToken() {
    String expiredToken = JWT.create().withIssuer(APP_NAME).withSubject(EMAIL)
        .withExpiresAt(Instant.now().minusSeconds(3600)).sign(Algorithm.HMAC256(SECRET));
    assertNull(tokenService.validateToken(expiredToken));
  }
}