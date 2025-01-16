package com.fiap.tech.payment.infra.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fiap.tech.payment.application.create.DefaultCreatePaymentUseCase;
import com.fiap.tech.payment.application.retrieve.get.DefaultGetPaymentByIdUseCase;
import com.fiap.tech.payment.domain.payment.PaymentGateway;
import com.fiap.tech.payment.infra.service.impl.WebhookPaymentService;

@Configuration
public class PaymentUseCaseConfiguration {

    private PaymentGateway paymentGateway;
    private WebhookPaymentService webhookPaymentService;   

    public PaymentUseCaseConfiguration(PaymentGateway paymentGateway, WebhookPaymentService webhookPaymentService) {
        this.paymentGateway = paymentGateway;
        this.webhookPaymentService = webhookPaymentService;
    }

    @Bean
    public DefaultCreatePaymentUseCase createPaymentUseCase() {
        return new DefaultCreatePaymentUseCase(paymentGateway, webhookPaymentService);
    }

    @Bean 
    public DefaultGetPaymentByIdUseCase getPaymentUseCase() {
        return new DefaultGetPaymentByIdUseCase(paymentGateway);
    }
}