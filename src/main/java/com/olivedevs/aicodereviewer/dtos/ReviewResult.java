package com.olivedevs.aicodereviewer.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResult {
    private String summary;
    private String verdict;
    private List<ReviewComment> comments;
}
