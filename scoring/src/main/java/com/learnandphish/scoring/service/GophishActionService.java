package com.learnandphish.scoring.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GophishActionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GOPHISH_API_URL = "http://gophish:3333/api/campaigns/";
    
    @Value("${GOPHISH_API_KEY}")
    private String gophishApiKey;

    public double getUserOverallScore(String email) {
        List<JsonNode> actions = getUserActions(email);
        if(actions.isEmpty()) return 0;
        
        // Group events by campaign_id
        Map<Integer, Integer> campaignScores = actions.stream()
            .collect(Collectors.groupingBy(
                event -> event.get("campaign_id").asInt(),
                Collectors.mapping(
                    event -> {
                        String msg = event.get("message").asText();
                        // Map event messages to severity scores
                        if ("Submitted Data".equalsIgnoreCase(msg)) return 10;
                        if ("Clicked Link".equalsIgnoreCase(msg)) return 4;
                        if ("Email Opened".equalsIgnoreCase(msg)) return 1;
                        return 0;
                    },
                    Collectors.reducing(0, Integer::max)
                )
             ));
        if(campaignScores.isEmpty()) return 0;
        
        // Compute average score per campaign (ratio out of 10)
        double totalScore = campaignScores.values().stream().mapToInt(Integer::intValue).sum();
        double avgScore = totalScore / campaignScores.size();
        return avgScore;
    }

    public List<JsonNode> getUserActions(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", gophishApiKey);
        HttpEntity<Void> reqEntity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(GOPHISH_API_URL, HttpMethod.GET, reqEntity, JsonNode.class);
        JsonNode body = response.getBody();
        if(body == null) return Collections.emptyList();

        List<JsonNode> filteredEvents = new ArrayList<>();
        body.forEach(campaign -> {
            JsonNode timeline = campaign.path("timeline");
            if (timeline.isArray()) {
                StreamSupport.stream(timeline.spliterator(), false)
                    .filter(event -> event.path("email").asText().equalsIgnoreCase(email))
                    .forEach(filteredEvents::add);
            }
        });
        return filteredEvents;
    }
}
