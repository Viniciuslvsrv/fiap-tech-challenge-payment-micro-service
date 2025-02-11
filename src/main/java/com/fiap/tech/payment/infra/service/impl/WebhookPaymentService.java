package com.fiap.tech.payment.infra.service.impl;


import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fiap.tech.payment.domain.payment.Payment;

public class WebhookPaymentService {

    private final RestTemplate restTemplate;

    public WebhookPaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> processPayment(Payment payment) {
        
        String payload = payment.toString();

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8083/webhook/payment-notification", payload, String.class);

        return response;
    }
}
