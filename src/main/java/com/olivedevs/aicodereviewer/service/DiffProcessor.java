package com.olivedevs.aicodereviewer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class DiffProcessor {

    private final WebClient webClient;

    public DiffProcessor(@Qualifier("githubWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public String fetchDiffPR(String diffUrl) {
        if (!StringUtils.hasText(diffUrl)) {
            throw new IllegalArgumentException("Pull request diff URL must not be empty");
        }
        try {
            String response = webClient.get()
                    .uri(diffUrl)
                    .accept(MediaType.TEXT_PLAIN)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null) {
                throw new IllegalStateException("GitHub returned an empty diff response");
            }

            log.info("Fetched PR diff with {} characters", response.length());
            return response;
        } catch (WebClientResponseException e) {
            throw new IllegalStateException(
                    "Failed to fetch PR diff from GitHub. Status: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to fetch PR diff from GitHub", e);
        }
    }
}
