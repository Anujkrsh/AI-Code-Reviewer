package com.olivedevs.aicodereviewer.controller;

import com.olivedevs.aicodereviewer.dtos.github.GithubPullRequestEvent;
import com.olivedevs.aicodereviewer.security.GithubWebhookEventValidator;
import com.olivedevs.aicodereviewer.security.GithubWebhookSignatureValidator;
import com.olivedevs.aicodereviewer.service.ReviewPipelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pr-review")
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private final GithubWebhookSignatureValidator validator;

    private final GithubWebhookEventValidator eventValidator;

    private final ReviewPipelineService service;

    @PostMapping("/handler")
    public ResponseEntity<String> reviewHandler(@RequestBody String payload, @RequestHeader("X-Hub-Signature-256") String sign,
                                                @RequestHeader(value="source",required = false) String source){
        if(source!=null && source.equals("local")){
            log.info("Skipping signature validation for local testing");
        }else{
            validator.validateSignature(payload, sign);
        }
        GithubPullRequestEvent event = eventValidator.validateEvent(payload);
        service.performReview(event);
        return ResponseEntity.ok("Webhook received");
    }
}