package com.olivedevs.aicodereviewer.service.Impl;

import com.olivedevs.aicodereviewer.dtos.PromptTemplate;
import com.olivedevs.aicodereviewer.dtos.ReviewResult;
import com.olivedevs.aicodereviewer.dtos.github.GithubPullRequestEvent;
import com.olivedevs.aicodereviewer.dtos.github.GithubResponse;
import com.olivedevs.aicodereviewer.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewPipelineImpl implements ReviewPipelineService {

    private final DiffProcessor diffProcessor;

    private final GeminiCall smokeTest;

    private final GithubStringFormatter formatter;

    private final FinalGithubCall lastCall;

    @Async("asyncTaskExecutor")
    public void performReview(GithubPullRequestEvent event){
        log.info("Review process started for payload at {} ",System.nanoTime());
        String diffUrl= event.getPullRequest().getDiffUrl();
        log.info("Diff url is: {}",diffUrl);
        String fetchedPR= diffProcessor.fetchDiffPR(diffUrl);
        String Prompt = PromptTemplate.CODE_REVIEW_PROMPT.formatted(fetchedPR);
        ReviewResult reviewResult = smokeTest.testConnection(Prompt);
        GithubResponse response = formatter.formatter(reviewResult);
        lastCall.finalGithubComment(response,event);
        log.info("Review process completed for payload at {} ", System.nanoTime());
    }
}
