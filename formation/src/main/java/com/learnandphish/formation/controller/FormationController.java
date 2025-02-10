package com.learnandphish.formation.controller;

import com.learnandphish.formation.dto.UserQuizScoreDTO;
import com.learnandphish.formation.model.*;
import com.learnandphish.formation.service.BadgeService;
import com.learnandphish.formation.service.FormationService;
import com.learnandphish.formation.service.QuizService;
import com.learnandphish.formation.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getFormationById(@PathVariable Integer formationId) {
        Formation formation = formationService.getFormationById(formationId);
        return formation != null ? ResponseEntity.ok(formation) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Formation not found");
    }

    // Get all quizzes
    @GetMapping("/quizzes")
    public ResponseEntity<List<Quiz>> getAllQuiz() {
        Iterable<Quiz> Quiz = quizService.getAllQuiz();
        return ResponseEntity.ok((List<Quiz>) Quiz);
    }


    // Get a quiz by id
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<?> getQuizById(@PathVariable Integer quizId) {
        Quiz quiz = quizService.getQuizById(quizId);
        return quiz != null ? ResponseEntity.ok(quiz) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
    }

    // Get all videos
    @GetMapping("/videos")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    // Set isWatched to true for a user and a video
    @PostMapping("/video/watched/{videoId}")
    public ResponseEntity<String> setIsWatched(@RequestHeader("email") String email, @PathVariable Integer videoId){
        Video video = videoService.getVideoById(videoId);
        if (video == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found");
        }
        videoService.setIsWatched(email, videoId);
        return ResponseEntity.ok("Video watched");
    }

    // Get all videos watched by a user
    @GetMapping("/videos/watched")
    public ResponseEntity<List<Video>> getVideosWatchedByUser(@RequestHeader("email") String email){
        List<Video> videos = videoService.getVideosWatchedByUser(email);
        return ResponseEntity.ok(videos);
    }

    // Save user score
    @PostMapping("/quiz/score")
    public ResponseEntity<String> saveUserScore(@RequestBody UserQuizScoreDTO userQuizScoreDTO){
        if (userQuizScoreDTO.getUserEmail() == null || userQuizScoreDTO.getQuizId() == null || userQuizScoreDTO.getScore() == null){
            return ResponseEntity.badRequest().body("Invalid request");
        }
        if (userQuizScoreDTO.getScore() < 0 || userQuizScoreDTO.getScore() > 1){
            return ResponseEntity.badRequest().body("Score must be between 0 and 1");
        }
        quizService.saveUserScore(userQuizScoreDTO.getUserEmail(), userQuizScoreDTO.getQuizId(), userQuizScoreDTO.getScore());
        return ResponseEntity.ok("Score saved successfully");
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
    public ResponseEntity<?> getUserScoreForQuiz(@RequestHeader("email") String email, @PathVariable Integer quiz_id){
        Float score = quizService.getUserScoreForQuiz(email, quiz_id);
        if (score == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Score not found");
        }
        return ResponseEntity.ok(score);
    }

    @PostMapping("/badge/claim/{badgeId}")
    public ResponseEntity<?> claimBadge(@PathVariable Integer badgeId, @RequestHeader("email") String email){
        Badge badge = badgeService.claimBadge(badgeId, email);
        if (badge == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Badge not found");
        }
        return ResponseEntity.ok(badge);
    }

    @GetMapping("/badge/all")
    public ResponseEntity<List<Badge>> getBadges(@RequestHeader("email") String email){
        List<Badge> badges = badgeService.getBadgesByUser(email);
        return ResponseEntity.ok(badges);
    }

    @GetMapping("/badge/{badgeId}")
    public ResponseEntity<?> getBadgeById(@PathVariable Integer badgeId){
        Badge badge = badgeService.getBadgeById(badgeId);
        return badge != null ? ResponseEntity.ok(badge) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Badge not found");
    }
}
