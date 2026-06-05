package com.olivedevs.aicodereviewer.dtos.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryDto {

    private Long id;

    @JsonProperty("full_name")
    private String fullName;
}
