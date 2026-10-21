package com.interview.tracker.service;

import com.interview.tracker.model.Question;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SpacedRepetitionService {

  /**
   * SM-2 Spaced Repetition Algorithm
   * Quality: 0-5 (0=complete blackout, 5=perfect response)
   * Updates question with new interval and ease factor
   */
  public void updateReview(Question question, int quality) {
    if (quality < 0 || quality > 5) {
      throw new IllegalArgumentException("Quality must be between 0 and 5");
    }

    // Clamp quality for calculations
    quality = Math.min(5, Math.max(0, quality));

    // Update repetition count
    question.setRepetitions(question.getRepetitions() + 1);

    // Track easy/hard counts
    if (quality >= 4) {
      question.setEasyCount(question.getEasyCount() + 1);
    }
    if (quality <= 2) {
      question.setHardCount(question.getHardCount() + 1);
    }

    // Update average quality
    int totalReviews = question.getRepetitions();
    int oldAvg = question.getAverageQuality();
    int newAvg = (oldAvg * (totalReviews - 1) + quality) / totalReviews;
    question.setAverageQuality(newAvg);

    // Update difficulty based on performance
    if (quality >= 4) {
      question.setDifficulty(Question.Difficulty.EASY);
    } else if (quality <= 2) {
      question.setDifficulty(Question.Difficulty.HARD);
    } else {
      question.setDifficulty(Question.Difficulty.MEDIUM);
    }

    // SM-2 algorithm calculations
    if (quality >= 3) {
      // Correct response - increase interval
      if (question.getRepetitions() == 1) {
        question.setInterval(1);
      } else if (question.getRepetitions() == 2) {
        question.setInterval(3);
      } else {
        int newInterval = (int) Math.round(question.getInterval() * question.getEaseFactor());
        question.setInterval(newInterval);
      }
    } else {
      // Incorrect response - reset
      question.setRepetitions(0);
      question.setInterval(1);
    }

    // Update ease factor
    double newEF = question.getEaseFactor() + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
    newEF = Math.max(1.3, newEF); // Minimum ease factor is 1.3
    question.setEaseFactor(newEF);

    // Schedule next review
    question.setLastReviewedAt(LocalDateTime.now());
    question.setNextReviewAt(LocalDateTime.now().plusDays(question.getInterval()));
  }

  /**
   * Calculate mastery score (0-100) based on performance
   */
  public int calculateMasteryScore(Question q) {
    if (q.getRepetitions() == 0) {
      return 0;
    }

    int baseScore = Math.min(q.getAverageQuality() * 20, 100); // Quality: 0-5 → 0-100
    int repeatBonus = Math.min(q.getRepetitions() * 5, 20); // Repetitions boost
    int easeBonus = Math.min((int) (q.getEaseFactor() - 1.3) * 10, 15); // Ease factor boost

    return Math.min(100, baseScore + repeatBonus + easeBonus);
  }

  /**
   * Get readiness status for next review
   */
  public String getReadinessStatus(Question q) {
    if (q.getNextReviewAt() == null) {
      return "READY";
    }

    LocalDateTime now = LocalDateTime.now();
    if (q.getNextReviewAt().isBefore(now)) {
      return "OVERDUE";
    } else if (q.getNextReviewAt().isBefore(now.plusDays(1))) {
      return "DUE_SOON";
    } else {
      return "SCHEDULED";
    }
  }
}
