package com.olivedevs.aicodereviewer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olivedevs.aicodereviewer.dtos.ReviewResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
public class GeminiCall {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String model;

    public GeminiCall(
            @Qualifier("geminiWebClient") WebClient webClient,
            ObjectMapper objectMapper,
            @Value("${gemini.model}") String model
    ) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.model = model;
    }

    public ReviewResult testConnection(String prompt) {

        Map<String, Object> request = Map.of(
                "model", model,
                "input", prompt
        );

        String response = webClient.post()
                .uri("/v1beta/interactions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Gemini Response: {}", response);
        return extractReviewResult(response);
    }

    private ReviewResult extractReviewResult(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode steps = root.path("steps");

            if (!steps.isArray()) {
                throw new IllegalStateException("Gemini response does not contain steps array");
            }

            for (JsonNode step : steps) {
                if (!"model_output".equals(step.path("type").asText())) {
                    continue;
                }

                JsonNode content = step.path("content");
                if (!content.isArray() || content.isEmpty()) {
                    throw new IllegalStateException("Gemini model_output does not contain content");
                }

                String reviewJson = content.get(0).path("text").asText();
                if (!StringUtils.hasText(reviewJson)) {
                    throw new IllegalStateException("Gemini model_output text is empty");
                }

                return objectMapper.readValue(stripJsonCodeFence(reviewJson), ReviewResult.class);
            }

            throw new IllegalStateException("Gemini response does not contain model_output step");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse Gemini review response", e);
        }
    }

    private String stripJsonCodeFence(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("```json")) {
            return trimmed.substring(7, trimmed.length() - 3).trim();
        }
        if (trimmed.startsWith("```")) {
            return trimmed.substring(3, trimmed.length() - 3).trim();
        }
        return trimmed;
    }
}
