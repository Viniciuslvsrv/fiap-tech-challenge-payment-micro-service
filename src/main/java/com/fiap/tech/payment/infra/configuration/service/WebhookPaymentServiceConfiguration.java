package com.fiap.tech.payment.infra.configuration.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fiap.tech.payment.infra.service.impl.WebhookPaymentService;

@Configuration
public class WebhookPaymentServiceConfiguration {
    
    @Bean
    public WebhookPaymentService webhookPaymentService() {
        return new WebhookPaymentService(
            new RestTemplate() 
        );
    }
}
