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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Quizz> createQuizz(@RequestBody QuizzRequest  quizzRequest) {
        Quizz createdQuizz = quizzService.createQuizz(quizzRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuizz);
    }

    @GetMapping
    public ResponseEntity<List<Quizz>> getAllQuizzs() {
        Iterable<Quizz> quizzs = quizzService.getAllQuizzs();
        return ResponseEntity.ok((List<Quizz>) quizzs);
    }

    @GetMapping("/{quizzId}")
    public ResponseEntity<Quizz> getQuizzById(@PathVariable Long quizzId) {
        Quizz quizz = quizzService.getQuizzById(quizzId);
        return quizz != null ? ResponseEntity.ok(quizz) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{quizzId}")
    public ResponseEntity<Void> deleteQuizz(@PathVariable Long quizzId) {
        quizzService.deleteQuizz(quizzId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Quizz> updateQuizz(@RequestBody QuizzRequest quizzRequest) {
        Quizz updatedQuizz = quizzService.updateQuizz(quizzRequest);
        return ResponseEntity.ok(updatedQuizz);
    }

    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<Quizz>> getQuizzByFormationId(@PathVariable Long formationId) {
        Iterable<Quizz> quizzs = quizzService.getQuizzByFormationId(formationId);
        return ResponseEntity.ok((List<Quizz>) quizzs);
    }

}
