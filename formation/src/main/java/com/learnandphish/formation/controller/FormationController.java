package com.learnandphish.formation.controller;

import com.learnandphish.formation.dto.UserQuizScoreDTO;
import com.learnandphish.formation.model.*;
import com.learnandphish.formation.service.FormationService;
import com.learnandphish.formation.service.QuizService;
import com.learnandphish.formation.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/formation")
@RequiredArgsConstructor
public class FormationController {
    private final FormationService formationService;
    private final QuizService quizService;
    private final VideoService videoService;

    // Get all formations
    @GetMapping("/formations")
    public ResponseEntity<List<Formation>> getAllFormations() {
        List<Formation> formations = formationService.getAllFormations();
        return ResponseEntity.ok(formations);
    }

    // Get a formation by id
    @GetMapping("/{formationId}")
    public ResponseEntity<Formation> getFormationById(@PathVariable Integer formationId) {
        Formation formation = formationService.getFormationById(formationId);
        return formation != null ? ResponseEntity.ok(formation) : ResponseEntity.notFound().build();
    }

    // Get all quizzes
    @GetMapping("/quiz")
    public ResponseEntity<List<Quiz>> getAllQuiz() {
        Iterable<Quiz> Quiz = quizService.getAllQuiz();
        return ResponseEntity.ok((List<Quiz>) Quiz);
    }


    // Get a quiz by id
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Integer quizId) {
        Quiz quiz = quizService.getQuizById(quizId);
        return quiz != null ? ResponseEntity.ok(quiz) : ResponseEntity.notFound().build();
    }

    // Get all videos
    @GetMapping("/videos")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    // Save user score
    @PostMapping("/quiz/score")
    public ResponseEntity<String> saveUserScore(@RequestBody UserQuizScoreDTO userQuizScoreDTO){
        quizService.saveUserScore(userQuizScoreDTO.getUser_email(), userQuizScoreDTO.getQuiz_id(), userQuizScoreDTO.getScore());
        return ResponseEntity.ok("Score saved successfully");
    }

    // Get user scores for all quizzes
    @GetMapping("/quiz/score/{user_email}")
    public ResponseEntity<List<UserQuizScoreDTO>> getUserScores(@PathVariable String user_email){
        ArrayList<UserQuizScoreDTO> userQuizScores = new ArrayList<>();
        Iterable<UserQuizScore> userQuizScoresList = quizService.getUserScores(user_email);
        for (UserQuizScore userQuizScore : userQuizScoresList){
            UserQuizScoreDTO userQuizScoreDTO = new UserQuizScoreDTO();
            userQuizScoreDTO.setUser_email(userQuizScore.getUserQuizId().getUserEmail());
            userQuizScoreDTO.setQuiz_id(userQuizScore.getUserQuizId().getQuizId());
            userQuizScoreDTO.setScore(userQuizScore.getScore());
            userQuizScores.add(userQuizScoreDTO);
        }
        return ResponseEntity.ok(userQuizScores);
    }

    // Get user score for a quiz
    @GetMapping("/quiz/score/{user_email}/{quiz_id}")
    public ResponseEntity<Float> getUserScoreForQuiz(@PathVariable String user_email, @PathVariable Integer quiz_id){
        Float score = quizService.getUserScoreForQuiz(user_email, quiz_id);
        return ResponseEntity.ok(score);
    }
}
