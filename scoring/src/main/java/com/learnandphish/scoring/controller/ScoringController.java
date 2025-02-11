package com.learnandphish.scoring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.learnandphish.scoring.dto.GophishLandingPageDTO;
import com.learnandphish.scoring.dto.QuizScoreDTO;
import com.learnandphish.scoring.service.FormationService;
import com.learnandphish.scoring.service.GophishActionService;
import com.learnandphish.scoring.service.SpiderFootScanService;
import com.learnandphish.scoring.service.OsintScoringService;
import com.learnandphish.scoring.service.AverageScoreService;
import com.learnandphish.scoring.service.TotalScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scoring")
@RequiredArgsConstructor
public class ScoringController {

    private final SpiderFootScanService spiderFootScanService;
    private final FormationService formationService;
    private final GophishActionService gophishActionService;
    private final OsintScoringService osintScoringService;
    private final AverageScoreService averageScoreService;
    private final TotalScoreService totalScoreService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/osint-results")
    public Map<String, Object> getResultCount(@RequestHeader("email") String email) {
        int count = spiderFootScanService.countResults(email);
        return Map.of("email", email, "resultCount", count);
    }

    @GetMapping("/formations")
    public List<QuizScoreDTO> getUserScores(@RequestHeader("email") String email) {
        return formationService.getUserQuizScores(email);
    }

    @GetMapping("/gophish-actions")
    public List<JsonNode> getGophishActions(@RequestHeader("email") String email) {
        return gophishActionService.getUserActions(email);
    }

    @GetMapping("/gophish-score")
    public Map<String, Object> getUserScore(@RequestHeader("email") String email) {
        double score = gophishActionService.getUserOverallScore(email);
        return Map.of("email", email, "score", score);
    }

    @GetMapping("/osint-score")
    public Map<String, Object> getOsintScore(@RequestHeader("email") String email) {
        double score = osintScoringService.getOsintScore(email);
        return Map.of("email", email, "osintScore", score);
    }

    @GetMapping("/formation-average")
    public Map<String, Object> getFormationAverageScore(@RequestHeader("email") String email) {
        double average = averageScoreService.getUserAverageScore(email);
        return Map.of("email", email, "formationAverage", average);
    }

    @GetMapping("/total-score")
    public Map<String, Object> getTotalScore(@RequestHeader("email") String email) {
        double totalScore = totalScoreService.getTotalScore(email);
        return Map.of("email", email, "totalScore", totalScore);
    }

    @PostMapping("/admin/total-score")
    public Map<String, Object> postTotalScore(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        double totalScore = totalScoreService.getTotalScore(email);
        return Map.of("email", email, "totalScore", totalScore);
    }

    @PostMapping("/admin/gophish-actions")
    public List<JsonNode> postGophishActions(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return gophishActionService.getUserActions(email);
    }
    
    @PostMapping("/admin/gophish-score")
    public Map<String, Object> postGophishScore(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        double score = gophishActionService.getUserOverallScore(email);
        return Map.of("email", email, "score", score);
    }
    
    @PostMapping("/admin/osint-score")
    public Map<String, Object> postOsintScore(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        double score = osintScoringService.getOsintScore(email);
        return Map.of("email", email, "osintScore", score);
    }

    @GetMapping("/admin/get-gophish-landing-pages")
    public List<GophishLandingPageDTO> getGophishTemplates() {
        return gophishActionService.getGophishLandingPages();
    }
}
