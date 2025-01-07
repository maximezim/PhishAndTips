package com.learnandphish.formation.dto;

public record VideoRequest(Long id, String formationId, String title, String description, String url, Integer duration, String quizId) {
}
