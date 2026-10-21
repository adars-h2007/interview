package com.interview.tracker.service;

import com.interview.tracker.model.User;
import com.interview.tracker.model.Question;
import com.interview.tracker.repository.QuestionRepository;
import com.interview.tracker.repository.UserRepository;
import com.interview.tracker.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final BCryptPasswordEncoder passwordEncoder;
  private final QuestionRepository questionRepository;

  public AuthService(UserRepository userRepository, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder,
      QuestionRepository questionRepository) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
    this.passwordEncoder = passwordEncoder;
    this.questionRepository = questionRepository;
  }

  @Transactional
  public User register(String name, String email, String password) {
    if (userRepository.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("Email already registered");
    }

    User user = User.builder()
        .name(name)
        .email(email)
        .password(passwordEncoder.encode(password))
        .build();

    user = userRepository.save(user);
    addStarterQuestionsIfNeeded(user);
    return user;
  }

  @Transactional
  public AuthResponse login(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new IllegalArgumentException("Invalid email or password");
    }

    addStarterQuestionsIfNeeded(user);

    String token = jwtUtil.generateToken(user.getId(), user.getEmail());
    return AuthResponse.builder()
        .token(token)
        .userId(user.getId())
        .email(user.getEmail())
        .name(user.getName())
        .build();
  }

  public User getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }

  private void addStarterQuestionsIfNeeded(User user) {
    if (Boolean.TRUE.equals(user.getStarterQuestionsAdded())) {
      return;
    }

    List<Question> starterQuestions = List.of(
        starterQuestion(user.getId(), "What is the time complexity of binary search?",
            "O(log n). Each comparison halves the remaining search space.", Question.Category.DSA,
            Question.Difficulty.EASY),
        starterQuestion(user.getId(), "What is the difference between an array and a linked list?",
            "Arrays store elements contiguously and support O(1) indexed access. Linked lists store nodes connected by pointers, making insertion and removal easier when the node is known.",
            Question.Category.DSA, Question.Difficulty.EASY),
        starterQuestion(user.getId(), "What does the CAP theorem say?",
            "When a network partition occurs, a distributed system must choose between consistency and availability; it cannot guarantee both at the same time.",
            Question.Category.SYSTEM_DESIGN, Question.Difficulty.MEDIUM),
        starterQuestion(user.getId(), "What are the ACID properties of a database transaction?",
            "Atomicity, Consistency, Isolation, and Durability: a transaction is all-or-nothing, preserves rules, runs safely alongside other work, and persists once committed.",
            Question.Category.DATABASE, Question.Difficulty.MEDIUM),
        starterQuestion(user.getId(), "How does TCP differ from UDP?",
            "TCP provides reliable, ordered delivery with connection setup. UDP sends independent datagrams with lower overhead but no delivery or ordering guarantee.",
            Question.Category.NETWORKING, Question.Difficulty.MEDIUM),
        starterQuestion(user.getId(), "Tell me about a time you handled a disagreement on a team.",
            "Use the STAR structure: describe the Situation and Task, explain the Action you took, and share the Result. Be specific about your contribution and what you learned.",
            Question.Category.BEHAVIORAL, Question.Difficulty.MEDIUM)
    );

    questionRepository.saveAll(starterQuestions);
    user.setStarterQuestionsAdded(true);
    userRepository.save(user);
  }

  private Question starterQuestion(Long userId, String question, String answer,
      Question.Category category, Question.Difficulty difficulty) {
    return Question.builder()
        .userId(userId)
        .question(question)
        .answer(answer)
        .category(category)
        .difficulty(difficulty)
        .build();
  }
}
