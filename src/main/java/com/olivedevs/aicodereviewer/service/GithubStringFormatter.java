package com.olivedevs.aicodereviewer.service;

import com.olivedevs.aicodereviewer.dtos.ReviewComment;
import com.olivedevs.aicodereviewer.dtos.ReviewResult;
import com.olivedevs.aicodereviewer.dtos.github.GithubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GithubStringFormatter {

    public GithubResponse formatter(ReviewResult result){

        GithubResponse response = new GithubResponse();
        StringBuilder commentBody = new StringBuilder();
        commentBody.append("##  \uD83E\uDD16 AI Code Review Results\n");
        commentBody.append("### Summary: ").append(result.getSummary()).append("\n\n");
        commentBody.append("### Verdict: ").append(result.getVerdict()).append("\n\n");
        commentBody.append("### Comments:\n");
        int size= result.getComments().size();
        if(result.getComments().isEmpty()){
            commentBody.append("No issues found. Great job! \n");
        }else {
            for (int i = 0; i < size; i++) {
                ReviewComment comment = result.getComments().get(i);

                commentBody.append("#### ")
                        .append(i + 1)
                        .append(". ")
                        .append(comment.getSeverity())
                        .append("\n");

                commentBody.append("- File: ")
                        .append(comment.getFilePath())
                        .append("\n");

                commentBody.append("- Issue: ")
                        .append(comment.getIssue())
                        .append("\n");

                commentBody.append("- Suggestion: ")
                        .append(comment.getSuggestion())
                        .append("\n\n");
            }
        }
        response.setBody(commentBody.toString());
        log.info("Formatted response: {}", response);
        return response;

    }
}
