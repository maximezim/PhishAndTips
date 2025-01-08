package com.learnandphish.formation.controller;

import com.learnandphish.formation.dto.QuizzRequest;
import com.learnandphish.formation.model.Quizz;
import com.learnandphish.formation.service.QuizzService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizz")
@RequiredArgsConstructor
public class QuizzController {
    private final QuizzService quizzService;

    @GetMapping
    public ResponseEntity<List<Quizz>> getAllQuizzs() {
        Iterable<Quizz> quizzs = quizzService.getAllQuizzs();
        return ResponseEntity.ok((List<Quizz>) quizzs);
    }

    /*@GetMapping("/formation/{formationId}")
    public ResponseEntity<List<Quizz>> getQuizzByFormationId(@PathVariable Long formationId) {
        Iterable<Quizz> quizzs = quizzService.getQuizzByFormationId(formationId);
        return ResponseEntity.ok((List<Quizz>) quizzs);
    }*/

}
