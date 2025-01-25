package com.fiap.tech.payment.infra.api.controller.webhook;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@Tag(name = "Webhook Controller", description = "Receives payment notifications")
@Hidden
public class WebhookController {

    @PostMapping("/payment-notification")
    @Operation(summary = "Receives payment notifications")
    public ResponseEntity<String> handlePaymentNotification(String payload) {
        try {
            return ResponseEntity.ok("Notification processed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
