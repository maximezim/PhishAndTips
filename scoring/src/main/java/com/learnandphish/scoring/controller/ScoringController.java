package com.learnandphish.scoring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.learnandphish.scoring.dto.QuizScoreDTO;
import com.learnandphish.scoring.service.FormationService;
import com.learnandphish.scoring.service.GophishActionService;
import com.learnandphish.scoring.service.SpiderFootScanService;
import com.learnandphish.scoring.service.OsintScoringService;
import com.learnandphish.scoring.service.AverageScoreService;
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
}
