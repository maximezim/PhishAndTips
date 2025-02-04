package com.learnandphish.scoring.controller;

import com.learnandphish.scoring.service.FormationService;
import com.learnandphish.scoring.service.SpiderFootScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/scoring")
@RequiredArgsConstructor
public class ScoringController {

    private final SpiderFootScanService spiderFootScanService;
    private final FormationService formationService;

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
    public Map<String, Object> getFormations(@RequestHeader("email") String email) {
        return Map.of("email", email, "formations", formationService.getUserFormations(email));
    }

}
