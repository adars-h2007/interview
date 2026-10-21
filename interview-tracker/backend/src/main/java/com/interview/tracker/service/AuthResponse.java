package com.interview.tracker.service;

import lombok.*;

@Data
@Builder
public class AuthResponse {
  private String token;
  private Long userId;
  private String email;
  private String name;

  public AuthResponse() {
  }

  public AuthResponse(String token, Long userId, String email, String name) {
    this.token = token;
    this.userId = userId;
    this.email = email;
    this.name = name;
  }
}
