package com.olivedevs.aicodereviewer.dtos.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestDto {

    private Long number;

    @JsonProperty("diff_url")
    private String diffUrl;
}
