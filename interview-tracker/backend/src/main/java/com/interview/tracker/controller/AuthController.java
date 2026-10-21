package com.interview.tracker.controller;

import com.interview.tracker.model.User;
import com.interview.tracker.service.AuthService;
import com.interview.tracker.service.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
    if (req.getEmail() == null || req.getPassword() == null || req.getName() == null) {
      throw new IllegalArgumentException("Name, email, and password are required");
    }
    User user = authService.register(req.getName(), req.getEmail(), req.getPassword());
    AuthResponse response = AuthResponse.builder()
        .userId(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .token("")
        .build();
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
    if (req.getEmail() == null || req.getPassword() == null) {
      throw new IllegalArgumentException("Email and password are required");
    }
    AuthResponse response = authService.login(req.getEmail(), req.getPassword());
    return ResponseEntity.ok(response);
  }

  public static class RegisterRequest {
    public String name;
    public String email;
    public String password;

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
  }

  public static class LoginRequest {
    public String email;
    public String password;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
  }
}
