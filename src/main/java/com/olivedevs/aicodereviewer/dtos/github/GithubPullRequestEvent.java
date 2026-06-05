package com.olivedevs.aicodereviewer.dtos.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubPullRequestEvent {
    private String action;

    private Long number;

    private RepositoryDto repository;

    @JsonProperty("pull_request")
    private PullRequestDto pullRequest;
}
