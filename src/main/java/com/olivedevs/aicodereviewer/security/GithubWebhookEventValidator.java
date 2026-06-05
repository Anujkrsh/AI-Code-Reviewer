package com.olivedevs.aicodereviewer.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olivedevs.aicodereviewer.dtos.github.GithubPullRequestEvent;
import com.olivedevs.aicodereviewer.exception.InvalidWebhookPayloadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubWebhookEventValidator {

    private final ObjectMapper objectMapper;

    public GithubPullRequestEvent validateEvent(String payload) {

        GithubPullRequestEvent event;
        try {
            event = objectMapper.readValue(
                    payload,
                    GithubPullRequestEvent.class
            );
        } catch (Exception e) {
            throw new InvalidWebhookPayloadException("Invalid Event Received");
        }
        return event;
    }
}
