package com.learnandphish.scoring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.learnandphish.scoring.dto.GophishLandingPageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
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
        return totalScore / campaignScores.size();
    }

    public Map<String, String> getUserActionCounts(String email) {
        List<JsonNode> actions = getUserActions(email);
        long totalSent = actions.stream()
            .filter(event -> "Email Sent".equalsIgnoreCase(event.get("message").asText()))
            .count();
            
        Map<String, Long> counts = actions.stream()
            .filter(event -> {
                String msg = event.get("message").asText();
                return "Submitted Data".equalsIgnoreCase(msg)
                    || "Clicked Link".equalsIgnoreCase(msg)
                    || "Email Opened".equalsIgnoreCase(msg);
            })
            .collect(Collectors.groupingBy(
                event -> event.get("message").asText(),
                Collectors.counting()
            ));

        Map<String, String> result = new HashMap<>();
        result.put("Submitted Data", counts.getOrDefault("Submitted Data", 0L) + "/" + totalSent);
        result.put("Clicked Link", counts.getOrDefault("Clicked Link", 0L) + "/" + totalSent);
        result.put("Email Opened", counts.getOrDefault("Email Opened", 0L) + "/" + totalSent);
        return result;
    }

    public List<JsonNode> getUserActions(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", gophishApiKey);
        HttpEntity<Void> reqEntity = new HttpEntity<>(headers);

        try {
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
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Invalid Gophish API key provided", e);
            }
            throw new ResponseStatusException(e.getStatusCode(), "Error calling Gophish API: " + e.getMessage(), e);
        }
    }

    public List<GophishLandingPageDTO> getGophishLandingPages(){
            File folder = new File("/var/gophish/landing-pages");
            File[] listOfFiles = folder.listFiles();
            if(listOfFiles == null) return Collections.emptyList();
            return Arrays.stream(listOfFiles)
                .map(file -> {
                    GophishLandingPageDTO dto = new GophishLandingPageDTO();
                    dto.setName(StringUtils.capitalize(file.getName()));
                    dto.setUrl("http://api-gateway:8080/api/static/endpoint/"+file.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
