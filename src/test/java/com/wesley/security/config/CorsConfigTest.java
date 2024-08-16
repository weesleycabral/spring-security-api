package com.wesley.security.config;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import com.wesley.security.config.cors.CorsConfig;

public class CorsConfigTest {

  @Mock
  private CorsRegistry corsRegistry;

  @Mock
  private CorsRegistration corsRegistration;

  private CorsConfig corsConfig;

  private final String URL = "http://localhost:4200";

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    corsConfig = new CorsConfig();
  }

  @Test
  public void testAddCorsMappings() {
    when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);
    when(corsRegistration.allowedOrigins(URL)).thenReturn(corsRegistration);
    when(corsRegistration.allowedMethods("GET", "POST", "DELETE", "PUT")).thenReturn(corsRegistration);

    corsConfig.addCorsMappings(corsRegistry);

    verify(corsRegistry).addMapping("/**");
    verify(corsRegistration).allowedOrigins(URL);
    verify(corsRegistration).allowedMethods("GET", "POST", "DELETE", "PUT");
  }
}