package com.olivedevs.aicodereviewer.exception.handler;

import com.olivedevs.aicodereviewer.dtos.ErrorResponse;
import com.olivedevs.aicodereviewer.exception.InvalidWebhookPayloadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidWebhookPayloadException.class)
    public ResponseEntity<ErrorResponse> handleInvalidWebhookPayload(InvalidWebhookPayloadException ex){

        ErrorResponse
                errorResponse =  ErrorResponse.builder().dateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .status("Webhook Payload is Invalid")
                .errorCode(ErrorCodes.INVALID_WEBHOOK_PAYLOAD).build();


        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
