package com.interview.tracker.controller;

import com.interview.tracker.model.Question;
import com.interview.tracker.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin
public class QuestionController {
  private final QuestionService questionService;

  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  private Long getUserId(HttpServletRequest request) {
    Object userId = request.getAttribute("userId");
    if (userId == null) {
      throw new SecurityException("Unauthorized");
    }
    return (Long) userId;
  }

  @GetMapping
  public ResponseEntity<List<Question>> getAllQuestions(HttpServletRequest request, @RequestParam(required = false) String category) {
    Long userId = getUserId(request);
    if (category != null && !category.isEmpty()) {
      return ResponseEntity.ok(questionService.getQuestionsByCategory(userId, Question.Category.valueOf(category)));
    }
    return ResponseEntity.ok(questionService.getAllQuestions(userId));
  }

  @GetMapping("/due")
  public ResponseEntity<List<Question>> getDueQuestions(HttpServletRequest request, @RequestParam(required = false) String category) {
    Long userId = getUserId(request);
    if (category != null && !category.isEmpty()) {
      return ResponseEntity.ok(questionService.getDueQuestionsByCategory(userId, Question.Category.valueOf(category)));
    }
    return ResponseEntity.ok(questionService.getDueQuestions(userId));
  }

  @GetMapping("/search")
  public ResponseEntity<List<Question>> searchQuestions(HttpServletRequest request, @RequestParam String q) {
    Long userId = getUserId(request);
    return ResponseEntity.ok(questionService.searchQuestions(userId, q));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Question> getQuestion(HttpServletRequest request, @PathVariable Long id) {
    Long userId = getUserId(request);
    return ResponseEntity.ok(questionService.getQuestion(id, userId));
  }

  @PostMapping
  public ResponseEntity<Question> createQuestion(HttpServletRequest request, @RequestBody Question question) {
    Long userId = getUserId(request);
    question.setUserId(userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(questionService.createQuestion(question));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Question> updateQuestion(HttpServletRequest request, @PathVariable Long id, @RequestBody Question updates) {
    Long userId = getUserId(request);
    return ResponseEntity.ok(questionService.updateQuestion(id, userId, updates));
  }

  @PostMapping("/{id}/review")
  public ResponseEntity<Question> reviewQuestion(HttpServletRequest request, @PathVariable Long id, @RequestBody Map<String, Integer> payload) {
    Long userId = getUserId(request);
    Integer quality = payload.get("quality");
    if (quality == null) {
      throw new IllegalArgumentException("quality is required");
    }
    return ResponseEntity.ok(questionService.reviewQuestion(id, userId, quality));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteQuestion(HttpServletRequest request, @PathVariable Long id) {
    Long userId = getUserId(request);
    questionService.getQuestion(id, userId);
    questionService.deleteQuestion(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/stats/all")
  public ResponseEntity<Map<String, Object>> getStats(HttpServletRequest request) {
    Long userId = getUserId(request);
    return ResponseEntity.ok(questionService.getStats(userId));
  }
}
