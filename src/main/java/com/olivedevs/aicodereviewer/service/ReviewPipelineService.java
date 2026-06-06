package com.olivedevs.aicodereviewer.service;

import com.olivedevs.aicodereviewer.dtos.github.GithubPullRequestEvent;

public interface ReviewPipelineService {

    public void performReview(GithubPullRequestEvent event);
}
