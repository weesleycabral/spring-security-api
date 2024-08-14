package com.wesley.security.services;

import java.time.Instant;

import com.wesley.security.entity.User;

public interface TokenService {

  public Instant generateExpirationDate();

  public String generateToken(User user);

  public String validateToken(String token);

}
