package com.learnandphish.formation.service;


import com.learnandphish.formation.model.Quiz;
import com.learnandphish.formation.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    // Get a quiz by id
    public Quiz getQuizById(Integer id) {
        return quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    // Get all quizzes
    public Iterable<Quiz> getAllQuiz() {
        return quizRepository.findAll();
    }
}
