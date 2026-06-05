package com.olivedevs.aicodereviewer.security;

import com.olivedevs.aicodereviewer.exception.InvalidWebhookPayloadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class GithubWebhookSignatureValidator {

    @Value("${github.webhook.secret}")
    private String webhookSecret;

    public void validateSignature(String payload, String signature) {
        try {
            String generatedSignature = generateSignature(payload);
            if(!generatedSignature.equals(signature)){
                throw new InvalidWebhookPayloadException("Invalid signature");
            }
        } catch (InvalidWebhookPayloadException e) {
            throw new InvalidWebhookPayloadException("Invalid Signature");
        }catch (Exception e) {
            throw new RuntimeException("Failed to validate webhook signature");
        }
    }

    private String generateSignature(String payload) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8)
                , "HmacSHA256");

        mac.init(secretKeySpec);

        byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return "sha256=" + hexString;
    }
}
