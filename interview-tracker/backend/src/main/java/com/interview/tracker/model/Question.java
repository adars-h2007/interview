package com.interview.tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@Data
@Builder
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String question;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String answer;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Category category;

  @Enumerated(EnumType.STRING)
  private Difficulty difficulty;

  @Builder.Default
  private Integer repetitions = 0;
  @Builder.Default
  private Double easeFactor = 2.5;
  @Builder.Default
  @Column(name = "`interval`")
  private Integer interval = 1;

  @Builder.Default
  private Integer easyCount = 0;
  @Builder.Default
  private Integer hardCount = 0;
  @Builder.Default
  private Integer averageQuality = 0;

  private LocalDateTime createdAt;
  private LocalDateTime lastReviewedAt;
  private LocalDateTime nextReviewAt;

  public Question() {
  }

  public Question(Long id, Long userId, String question, String answer, Category category,
      Difficulty difficulty, Integer repetitions, Double easeFactor, Integer interval,
      Integer easyCount, Integer hardCount, Integer averageQuality, LocalDateTime createdAt,
      LocalDateTime lastReviewedAt, LocalDateTime nextReviewAt) {
    this.id = id;
    this.userId = userId;
    this.question = question;
    this.answer = answer;
    this.category = category;
    this.difficulty = difficulty;
    this.repetitions = repetitions;
    this.easeFactor = easeFactor;
    this.interval = interval;
    this.easyCount = easyCount;
    this.hardCount = hardCount;
    this.averageQuality = averageQuality;
    this.createdAt = createdAt;
    this.lastReviewedAt = lastReviewedAt;
    this.nextReviewAt = nextReviewAt;
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    if (this.nextReviewAt == null) {
      this.nextReviewAt = LocalDateTime.now();
    }
    if (this.difficulty == null) {
      this.difficulty = Difficulty.MEDIUM;
    }
    if (this.category == null) {
      this.category = Category.OTHER;
    }
  }

  public enum Category {
    DSA("Data Structures & Algorithms"),
    JAVA("Java"),
    WEB_DEVELOPMENT("Web Development"),
    SYSTEM_DESIGN("System Design"),
    BEHAVIORAL("Behavioral"),
    DATABASE("Database"),
    NETWORKING("Networking"),
    CLOUD("Cloud & DevOps"),
    OTHER("Other");

    public final String label;
    Category(String label) {
      this.label = label;
    }
  }

  public enum Difficulty {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard");

    public final String label;
    Difficulty(String label) {
      this.label = label;
    }
  }
}
