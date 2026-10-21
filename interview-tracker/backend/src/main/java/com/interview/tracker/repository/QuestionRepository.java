package com.interview.tracker.repository;

import com.interview.tracker.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
  List<Question> findByUserIdAndCategory(Long userId, Question.Category category);

  List<Question> findByUserId(Long userId);

  @Query("SELECT q FROM Question q WHERE q.userId = :userId AND q.nextReviewAt <= :now ORDER BY q.nextReviewAt ASC")
  List<Question> findDueQuestions(Long userId, LocalDateTime now);

  @Query("SELECT q FROM Question q WHERE q.userId = :userId AND q.category = :category AND q.nextReviewAt <= :now ORDER BY q.nextReviewAt ASC")
  List<Question> findDueQuestionsByCategory(Long userId, Question.Category category, LocalDateTime now);

  @Query("SELECT q FROM Question q WHERE q.userId = :userId AND (LOWER(q.question) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(q.answer) LIKE LOWER(CONCAT('%', :query, '%')))")
  List<Question> searchQuestions(Long userId, String query);

  @Query("SELECT COUNT(q) FROM Question q WHERE q.userId = :userId AND q.nextReviewAt <= :now")
  Long countDueQuestions(Long userId, LocalDateTime now);
}
