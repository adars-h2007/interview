package com.interview.tracker.repository;

import com.interview.tracker.model.ReviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewHistoryRepository extends JpaRepository<ReviewHistory, Long> {
  List<ReviewHistory> findByUserIdOrderByReviewedAtDesc(Long userId);
  List<ReviewHistory> findByQuestionIdAndUserIdOrderByReviewedAtDesc(Long questionId, Long userId);
}
