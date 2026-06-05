package com.olivedevs.aicodereviewer.exception;


public class InvalidWebhookPayloadException extends RuntimeException{


    public InvalidWebhookPayloadException(String message) {
        super(message);
    }
}
