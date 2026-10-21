package com.interview.tracker.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_history", indexes = {
    @Index(name = "idx_review_user_reviewed", columnList = "user_id, reviewed_at"),
    @Index(name = "idx_review_question", columnList = "question_id")
})
@Data
@Builder
public class ReviewHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "question_id", nullable = false)
  private Long questionId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Integer quality;

  @Column(name = "reviewed_at", nullable = false)
  private LocalDateTime reviewedAt;

  public ReviewHistory() {}

  public ReviewHistory(Long id, Long questionId, Long userId, Integer quality, LocalDateTime reviewedAt) {
    this.id = id;
    this.questionId = questionId;
    this.userId = userId;
    this.quality = quality;
    this.reviewedAt = reviewedAt;
  }
}
