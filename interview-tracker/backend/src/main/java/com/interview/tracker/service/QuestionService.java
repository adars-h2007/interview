package com.interview.tracker.service;

import com.interview.tracker.model.Question;
import com.interview.tracker.repository.QuestionRepository;
import com.interview.tracker.model.ReviewHistory;
import com.interview.tracker.repository.ReviewHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionService {
  private final QuestionRepository questionRepository;
  private final SpacedRepetitionService srService;
  private final ReviewHistoryRepository reviewHistoryRepository;

  public QuestionService(QuestionRepository questionRepository, SpacedRepetitionService srService,
      ReviewHistoryRepository reviewHistoryRepository) {
    this.questionRepository = questionRepository;
    this.srService = srService;
    this.reviewHistoryRepository = reviewHistoryRepository;
  }

  public Question createQuestion(Question question) {
    if (question.getCategory() == null) {
      question.setCategory(Question.Category.OTHER);
    }
    if (question.getDifficulty() == null) {
      question.setDifficulty(Question.Difficulty.MEDIUM);
    }
    return questionRepository.save(question);
  }

  public Question getQuestion(Long id, Long userId) {
    Question q = questionRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Question not found"));
    if (!q.getUserId().equals(userId)) {
      throw new SecurityException("Unauthorized");
    }
    return q;
  }

  public List<Question> getAllQuestions(Long userId) {
    return questionRepository.findByUserId(userId);
  }

  public List<Question> getQuestionsByCategory(Long userId, Question.Category category) {
    return questionRepository.findByUserIdAndCategory(userId, category);
  }

  public List<Question> getDueQuestions(Long userId) {
    return questionRepository.findDueQuestions(userId, LocalDateTime.now());
  }

  public List<Question> getDueQuestionsByCategory(Long userId, Question.Category category) {
    return questionRepository.findDueQuestionsByCategory(userId, category, LocalDateTime.now());
  }

  public List<Question> searchQuestions(Long userId, String query) {
    if (query == null || query.trim().isEmpty()) {
      return getAllQuestions(userId);
    }
    return questionRepository.searchQuestions(userId, query);
  }

  public Question reviewQuestion(Long id, Long userId, int quality) {
    Question q = getQuestion(id, userId);
    LocalDateTime reviewedAt = LocalDateTime.now();
    srService.updateReview(q, quality);
    Question reviewed = questionRepository.save(q);
    reviewHistoryRepository.save(ReviewHistory.builder()
        .questionId(reviewed.getId())
        .userId(userId)
        .quality(quality)
        .reviewedAt(reviewedAt)
        .build());
    return reviewed;
  }

  public Question updateQuestion(Long id, Long userId, Question updates) {
    Question q = getQuestion(id, userId);
    if (updates.getQuestion() != null) {
      q.setQuestion(updates.getQuestion());
    }
    if (updates.getAnswer() != null) {
      q.setAnswer(updates.getAnswer());
    }
    if (updates.getCategory() != null) {
      q.setCategory(updates.getCategory());
    }
    if (updates.getDifficulty() != null) {
      q.setDifficulty(updates.getDifficulty());
    }
    return questionRepository.save(q);
  }

  public void deleteQuestion(Long id) {
    questionRepository.deleteById(id);
  }

  public Map<String, Object> getStats(Long userId) {
    List<Question> all = getAllQuestions(userId);
    List<Question> due = getDueQuestions(userId);
    long masteredCount = all.stream()
        .filter(q -> srService.calculateMasteryScore(q) >= 80)
        .count();

    double avgMastery = all.stream()
        .mapToInt(srService::calculateMasteryScore)
        .average()
        .orElse(0);

    return Map.of(
        "totalQuestions", all.size(),
        "dueQuestions", due.size(),
        "masteredQuestions", masteredCount,
        "averageMastery", Math.round(avgMastery * 100.0) / 100.0,
        "categoryBreakdown", getCategoryStats(userId)
    );
  }

  private Map<String, Integer> getCategoryStats(Long userId) {
    return Arrays.stream(Question.Category.values())
        .collect(Collectors.toMap(
            c -> c.name(),
            c -> (int) getQuestionsByCategory(userId, c).size()
        ));
  }
}
