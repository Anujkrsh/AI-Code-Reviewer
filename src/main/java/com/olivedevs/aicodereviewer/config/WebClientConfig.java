package com.olivedevs.aicodereviewer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
public class WebClientConfig {

    @Value("${github.token}")
    private String auth_token;

    @Value("${gemini.key}")
    private String geminiApiKey;

    @Bean("githubWebClient")
    public WebClient githubWebClient(){
        HttpClient httpClient = HttpClient.create()
                .followRedirect(true);

        WebClient.Builder builder = WebClient.builder()
                .baseUrl("https://api.github.com")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Accept", "application/vnd.github.v3+json");

        if (StringUtils.hasText(auth_token)) {
            builder.defaultHeader("Authorization", "Bearer " + auth_token);
        }

        WebClient client = builder.build();
        log.info("Web client configured successfully {}",client);
        return client;
    }

    @Bean("geminiWebClient")
    public WebClient geminiWebClient() {
        if(geminiApiKey != null && !geminiApiKey.isBlank()) {
            log.info("API Key is not null and not Blank");
        }
        return WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader("x-goog-api-key", geminiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Api-Revision", "2026-05-20")
                .build();
    }
}
