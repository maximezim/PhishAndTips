package com.learnandphish.scoring.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class GophishActionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GOPHISH_API_URL = "http://gophish:3333/api/campaigns/";
    
    @Value("${GOPHISH_API_KEY}")
    private String gophishApiKey;

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
