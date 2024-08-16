package com.wesley.security.infra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.wesley.security.entity.User;
import com.wesley.security.repository.UserRepository;
import com.wesley.security.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class SecurityFilterTest {

  @Mock
  private TokenService tokenService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @InjectMocks
  private SecurityFilter securityFilter;

  @BeforeEach
  public void setup() {
    SecurityContextHolder.clearContext();
  }

  @Test
  public void testDoFilterInternalWithValidToken() throws ServletException, IOException {
    String token = "validToken";
    String userEmail = "user@example.com";
    User user = new User();
    user.setEmail(userEmail);

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(tokenService.validateToken(token)).thenReturn(userEmail);
    when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

    securityFilter.doFilterInternal(request, response, filterChain);

    var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    var expectedAuthentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

    assertEquals(expectedAuthentication, SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  public void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {
    String token = "invalidToken";

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(tokenService.validateToken(token)).thenReturn(null);

    securityFilter.doFilterInternal(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  public void testDoFilterInternalWithMissingToken() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn(null);

    securityFilter.doFilterInternal(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }
}