package com.olivedevs.aicodereviewer.service;

import com.olivedevs.aicodereviewer.dtos.github.GithubPullRequestEvent;
import com.olivedevs.aicodereviewer.dtos.github.GithubResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FinalGithubCall {


    private final WebClient client;

    public FinalGithubCall( @Qualifier("githubWebClient")WebClient client) {
        this.client = client;
    }

    public void finalGithubComment(GithubResponse commentBody, GithubPullRequestEvent event){

        String repo=event.getRepository().getFullName();
        client.post().uri("/repos/"+repo+"/issues/{prNumber}/comments",
                event.getPullRequest().getNumber())
                .bodyValue(commentBody)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
