package com.learnandphish.formation.dto;

public record VideoRequest(Long id, String title, String description, String url, Integer duration, Integer difficulty) {
}
