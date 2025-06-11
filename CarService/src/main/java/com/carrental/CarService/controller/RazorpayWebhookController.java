package com.carrental.CarService.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class RazorpayWebhookController {

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,
                                                @RequestHeader("X-Razorpay-Signature") String signature) {
        try {
            String payload = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            log.info("Received Razorpay webhook payload: {}", payload);

            if (!verifySignature(payload, signature, webhookSecret)) {
                log.warn("Invalid Razorpay webhook signature");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
            }

            JSONObject json = new JSONObject(payload);
            String event = json.getString("event");

            switch (event) {
                case "payment.captured":
                    log.info("✅ Payment captured: {}", json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity").getString("id"));
                    // TODO: Update booking status to PAID
                    break;
                case "payment.failed":
                    log.info("❌ Payment failed: {}", json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity").getString("id"));
                    // TODO: Mark booking as FAILED or delete
                    break;
                default:
                    log.info("Unhandled event type: {}", event);
            }

            return ResponseEntity.ok("Webhook processed");
        } catch (Exception e) {
            log.error("Error processing Razorpay webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook error");
        }
    }

    private boolean verifySignature(String payload, String actualSignature, String secret) throws Exception {
        String expectedSignature = hmacSha256(payload, secret);
        return expectedSignature.equals(actualSignature);
    }

    private String hmacSha256(String data, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(hash));
    }
}
