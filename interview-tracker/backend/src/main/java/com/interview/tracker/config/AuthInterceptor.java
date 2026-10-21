package com.interview.tracker.config;

import com.interview.tracker.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
  private final JwtUtil jwtUtil;

  public AuthInterceptor(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    // CORS preflight requests are sent without the bearer token; Spring handles
    // the origin/method/header checks using CorsConfig.
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return true;
    }

    // Skip auth for public endpoints
    String path = request.getRequestURI();
    if (path.startsWith("/api/auth/")) {
      return true;
    }

    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }

    String token = header.substring(7);
    if (!jwtUtil.validateToken(token)) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }

    Long userId = jwtUtil.getUserIdFromToken(token);
    request.setAttribute("userId", userId);
    return true;
  }
}
