package com.learnandphish.formation.controller;

import com.learnandphish.formation.model.Quiz;
import com.learnandphish.formation.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllquizs() {
        Iterable<Quiz> Quizs = quizService.getAllQuizs();
        return ResponseEntity.ok((List<Quiz>) Quizs);
    }

}
