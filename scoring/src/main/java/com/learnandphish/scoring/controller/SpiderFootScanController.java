package com.learnandphish.scoring.controller;

import com.learnandphish.scoring.service.SpiderFootScanService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class SpiderFootScanController {

    private final SpiderFootScanService spiderFootScanService;

    public SpiderFootScanController(SpiderFootScanService spiderFootScanService) {
        this.spiderFootScanService = spiderFootScanService;
    }

    @GetMapping("/scoring/results")
    public Map<String, Object> getResultCount(@RequestParam String target) {
        int count = spiderFootScanService.countResults(target);
        return Map.of("target", target, "resultCount", count);
    }
}
