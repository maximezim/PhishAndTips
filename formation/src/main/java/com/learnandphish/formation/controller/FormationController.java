package com.learnandphish.formation.controller;

import com.learnandphish.formation.model.Formation;
import com.learnandphish.formation.model.Quiz;
import com.learnandphish.formation.service.FormationService;
import com.learnandphish.formation.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formation")
@RequiredArgsConstructor
public class FormationController {
    private final FormationService formationService;
    private final QuizService quizService;


    @GetMapping("/formations")
    public ResponseEntity<List<Formation>> getAllFormations() {
        List<Formation> formations = formationService.getAllFormations();
        return ResponseEntity.ok(formations);
    }

    @GetMapping("/{formationId}")
    public ResponseEntity<Formation> getFormationById(@PathVariable Integer formationId) {
        Formation formation = formationService.getFormationById(formationId);
        return formation != null ? ResponseEntity.ok(formation) : ResponseEntity.notFound().build();
    }

    @GetMapping("/quiz")
    public ResponseEntity<List<Quiz>> getAllQuiz() {
        Iterable<Quiz> Quiz = quizService.getAllQuiz();
        return ResponseEntity.ok((List<Quiz>) Quiz);
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Integer quizId) {
        Quiz quiz = quizService.getQuizById(quizId);
        return quiz != null ? ResponseEntity.ok(quiz) : ResponseEntity.notFound().build();
    }
}
