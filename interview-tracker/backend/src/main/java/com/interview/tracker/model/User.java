package com.interview.tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Builder.Default
  @Column(name = "starter_questions_added", nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
  private Boolean starterQuestionsAdded = false;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public User() {
  }

  public User(Long id, String email, String password, String name, Boolean starterQuestionsAdded,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.starterQuestionsAdded = starterQuestionsAdded;
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
