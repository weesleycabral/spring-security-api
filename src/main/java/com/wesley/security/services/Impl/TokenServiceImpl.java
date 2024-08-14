package com.wesley.security.services.Impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wesley.security.entity.User;
import com.wesley.security.exception.ErrorCreatingTokenException;
import com.wesley.security.services.TokenService;

@Service
public class TokenServiceImpl implements TokenService {
  @Value("${api.security.token.secret}")
  private String secret;

  @Override
  public Instant generateExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }

  @Override
  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      String token = JWT.create().withIssuer("spring-security-app").withSubject(user.getEmail())
          .withExpiresAt(this.generateExpirationDate()).sign(algorithm);
      return token;
    } catch (JWTCreationException e) {
      throw new ErrorCreatingTokenException();
    }
  }

  @Override
  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm).withIssuer("spring-security-app").build().verify(token).getSubject();
    } catch (JWTVerificationException e) {
      return null;
    }
  }

}
