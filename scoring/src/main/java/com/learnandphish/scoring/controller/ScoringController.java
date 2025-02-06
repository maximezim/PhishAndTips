package com.learnandphish.scoring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.learnandphish.scoring.dto.QuizScoreDTO;
import com.learnandphish.scoring.service.FormationService;
import com.learnandphish.scoring.service.GophishActionService;
import com.learnandphish.scoring.service.SpiderFootScanService;
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
        return formationService.getUserFormations(email);
    }

    @GetMapping("/gophish-actions")
    public List<JsonNode> getGophishActions(@RequestHeader("email") String email) {
        return gophishActionService.getUserActions(email);
    }
}
