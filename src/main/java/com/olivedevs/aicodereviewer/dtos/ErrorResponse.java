package com.olivedevs.aicodereviewer.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {

    private LocalDateTime dateTime;

    private String status;

    private String errorCode;

    private String message;

}
