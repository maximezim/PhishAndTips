package com.learnandphish.formation.controller;

import com.learnandphish.formation.dto.UserQuizScoreDTO;
import com.learnandphish.formation.model.*;
import com.learnandphish.formation.service.BadgeService;
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
    private final BadgeService badgeService;

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
    @GetMapping("/quizzes")
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
    public ResponseEntity<String> saveUserScore(@RequestBody UserQuizScoreDTO userQuizScoreDTO) {
        try {
            quizService.saveUserScore(userQuizScoreDTO.getUserEmail(), userQuizScoreDTO.getQuizId(), userQuizScoreDTO.getScore());
            return ResponseEntity.ok("Score saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving score");
        }
    }

    // Get user scores for all quizzes
    @GetMapping("/quizzes/scores")
    public ResponseEntity<List<UserQuizScoreDTO>> getUserScores(@RequestHeader("email") String email){
        ArrayList<UserQuizScoreDTO> userQuizScores = new ArrayList<>();
        Iterable<UserQuizScore> userQuizScoresList = quizService.getUserScores(email);
        if (!userQuizScoresList.iterator().hasNext()) {
            return ResponseEntity.notFound().build();
        }
        for (UserQuizScore userQuizScore : userQuizScoresList){
            UserQuizScoreDTO userQuizScoreDTO = new UserQuizScoreDTO();
            userQuizScoreDTO.setUserEmail(userQuizScore.getUserQuizId().getUserEmail());
            userQuizScoreDTO.setQuizId(userQuizScore.getUserQuizId().getQuizId());
            userQuizScoreDTO.setScore(userQuizScore.getScore());
            userQuizScores.add(userQuizScoreDTO);
        }
        return ResponseEntity.ok(userQuizScores);
    }

    // Get user score for a quiz
    @GetMapping("/quiz/score/{quiz_id}")
    public ResponseEntity<Float> getUserScoreForQuiz(@RequestHeader("email") String email, @PathVariable Integer quiz_id){
        Float score = quizService.getUserScoreForQuiz(email, quiz_id);
        if (score == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(score);
    }

    @PostMapping("/badge/claim/{badgeId}")
    public ResponseEntity<Badge> claimBadge(@PathVariable Integer badgeId, @RequestHeader("email") String email){
        Badge badge = badgeService.claimBadge(badgeId, email);
        if (badge == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(badge);
    }

    @GetMapping("/badge/all")
    public ResponseEntity<List<Badge>> getBadges(@RequestHeader("email") String email){
        List<Badge> badges = badgeService.getBadgesByUser(email);
        return ResponseEntity.ok(badges);
    }

    @GetMapping("/badge/{badgeId}")
    public ResponseEntity<Badge> getBadgeById(@PathVariable Integer badgeId){
        Badge badge = badgeService.getBadgeById(badgeId);
        return badge != null ? ResponseEntity.ok(badge) : ResponseEntity.notFound().build();
    }
}
