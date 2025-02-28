package com.learnandphish.formation.controller;

import com.learnandphish.formation.dto.UserQuizScoreDTO;
import com.learnandphish.formation.model.*;
import com.learnandphish.formation.service.BadgeService;
import com.learnandphish.formation.service.QuizService;
import com.learnandphish.formation.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class FormationController {
    private final QuizService quizService;
    private final VideoService videoService;
    private final BadgeService badgeService;

    // Get all quizzes
    @GetMapping("/formation/quizzes")
    public ResponseEntity<List<Quiz>> getAllQuiz() {
        Iterable<Quiz> Quiz = quizService.getAllQuiz();
        return ResponseEntity.ok((List<Quiz>) Quiz);
    }

    // Get a quiz by id
    @GetMapping("/formation/quiz/{quizId}")
    public ResponseEntity<?> getQuizById(@PathVariable Integer quizId) {
        Quiz quiz = quizService.getQuizById(quizId);
        return ResponseEntity.ok(quiz != null ? quiz : new Quiz());
    }

    // Update a quiz
    @PutMapping("/admin/formation/quiz/{quizId}")
    public ResponseEntity<?> updateQuiz(@PathVariable Integer quizId, @RequestBody Quiz quiz) {
        Quiz updatedQuiz = quizService.updateQuiz(quizId, quiz);
        if (updatedQuiz == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quiz not found");
        }
        return ResponseEntity.ok("Quiz updated successfully");
    }

    // Create a quiz with error handling
    @PostMapping("/admin/formation/quiz")
    public ResponseEntity<?> createQuiz(@RequestBody Quiz quiz) {
        try {
            Quiz createdQuiz = quizService.createQuiz(quiz);
            return ResponseEntity.ok(Map.of("message", "Quiz created", "id", createdQuiz.getId()));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating quiz: " + e.getMessage());
        }
    }

    // Get all videos
    @GetMapping("/formation/videos")
    public ResponseEntity<List<Video>> getAllVideos() {
        List<Video> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }


    // Create a video with file upload
    @PostMapping("/admin/formation/video/upload")
    public ResponseEntity<?> uploadVideo(@RequestParam("videoFile") MultipartFile videoFile,
                                         @RequestParam("thumbnailFile") MultipartFile thumbnailFile,
                                         @RequestParam(value = "captionFile", required = false) MultipartFile captionFile,
                                         @RequestParam("title") String title,
                                         @RequestParam("description") String description) {
        try {
            Video video = new Video();
            video.setTitle(title);
            video.setDescription(description);
            video.setVideoUrl(videoService.sanitizeFileName(Objects.requireNonNull(videoFile.getOriginalFilename())));
            video.setThumbnailUrl(videoService.sanitizeFileName(Objects.requireNonNull(thumbnailFile.getOriginalFilename())));
            if (captionFile != null && !captionFile.isEmpty()){
                video.setCaptionUrl(videoService.sanitizeFileName(Objects.requireNonNull(captionFile.getOriginalFilename())));
            }

            Video createdVideo = videoService.createVideo(video, videoFile, thumbnailFile, captionFile);
            if (createdVideo == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating video");
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading video: " + e.getMessage());
        }
    }


    // Delete a video
    @DeleteMapping("/admin/formation/video/{videoId}")
    public ResponseEntity<?> deleteVideo(@PathVariable Integer videoId) {
        Video video = videoService.getVideoById(videoId);
        if (video == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found");
        }
        videoService.deleteVideo(videoId);
        return ResponseEntity.ok("Video deleted successfully");
    }

    // Set isWatched to true for a user and a video
    @PostMapping("/formation/video/watched/{videoId}")
    public ResponseEntity<String> setIsWatched(@RequestHeader("email") String email, @PathVariable Integer videoId){
        Video video = videoService.getVideoById(videoId);
        if (video == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found");
        }
        videoService.setIsWatched(email, videoId);
        return ResponseEntity.ok("Video watched");
    }

    // Get all videos watched by a user
    @GetMapping("/formation/videos/watched")
    public ResponseEntity<List<Video>> getVideosWatchedByUser(@RequestHeader("email") String email){
        List<Video> videos = videoService.getVideosWatchedByUser(email);
        return ResponseEntity.ok(videos);
    }

    // Save user score
    @PostMapping("/formation/quiz/score")
    public ResponseEntity<String> saveUserScore(@RequestBody UserQuizScoreDTO userQuizScoreDTO, @RequestHeader("email") String email){
        if (userQuizScoreDTO.getQuizId() == null || userQuizScoreDTO.getScore() == null || email == null){
            return ResponseEntity.badRequest().body("Invalid request");
        }
        if (userQuizScoreDTO.getScore() < 0 || userQuizScoreDTO.getScore() > 1){
            return ResponseEntity.badRequest().body("Score must be between 0 and 1");
        }
        quizService.saveUserScore(email, userQuizScoreDTO.getQuizId(), userQuizScoreDTO.getScore());
        return ResponseEntity.ok("Score saved successfully");
    }

    // Get user scores for all quizzes
    @GetMapping("/formation/quizzes/scores")
    public ResponseEntity<List<UserQuizScoreDTO>> getUserScores(@RequestHeader("email") String email){
        ArrayList<UserQuizScoreDTO> userQuizScores = new ArrayList<>();
        try {
            Iterable<UserQuizScore> userQuizScoresList = quizService.getUserScores(email);
            if(userQuizScoresList != null) {
                for (UserQuizScore userQuizScore : userQuizScoresList){
                    UserQuizScoreDTO userQuizScoreDTO = new UserQuizScoreDTO();
                    userQuizScoreDTO.setQuizId(userQuizScore.getUserQuizId().getQuizId());
                    userQuizScoreDTO.setScore(userQuizScore.getScore());
                    userQuizScores.add(userQuizScoreDTO);
                }
            }
        } catch (Exception ex) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        return ResponseEntity.ok(userQuizScores);
    }

    // Get user score for a quiz
    @GetMapping("/formation/quiz/score/{quiz_id}")
    public ResponseEntity<?> getUserScoreForQuiz(@RequestHeader("email") String email, @PathVariable Integer quiz_id){
        Float score = quizService.getUserScoreForQuiz(email, quiz_id);
        return ResponseEntity.ok(score != null ? score : 0.0f);
    }

    @PostMapping("/formation/badge/claim/{badgeId}")
    public ResponseEntity<?> claimBadge(@PathVariable Integer badgeId, @RequestHeader("email") String email){
        Badge badge = badgeService.claimBadge(badgeId, email);
        return ResponseEntity.ok(badge != null ? badge : new Badge());
    }

    @GetMapping("/formation/badge/all")
    public ResponseEntity<List<Badge>> getBadges(@RequestHeader("email") String email){
        List<Badge> badges = badgeService.getBadgesByUser(email);
        return ResponseEntity.ok(badges);
    }

    @GetMapping("/formation/badge/{badgeId}")
    public ResponseEntity<?> getBadgeById(@PathVariable Integer badgeId){
        Badge badge = badgeService.getBadgeById(badgeId);
        return ResponseEntity.ok(badge != null ? badge : new Badge());
    }

    // Upload a file
    @PostMapping("/admin/formation/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = videoService.uploadFile(file);
            if (fileUrl == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
            }
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }
}
