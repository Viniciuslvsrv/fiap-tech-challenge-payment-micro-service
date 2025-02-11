package com.fiap.tech.payment.application.create;

import org.springframework.http.ResponseEntity;

import com.fiap.tech.payment.domain.exception.NotificationException;
import com.fiap.tech.payment.domain.validation.handler.Notification;
import com.fiap.tech.payment.infra.service.impl.WebhookPaymentService;
import com.fiap.tech.payment.domain.payment.Payment;
import com.fiap.tech.payment.domain.payment.PaymentGateway;
import com.fiap.tech.payment.domain.payment.PaymentStatus;

public class DefaultCreatePaymentUseCase extends CreatePaymentUseCase {
    
    private final PaymentGateway paymentGateway;   
    private final WebhookPaymentService webhookPaymentService;    

    public DefaultCreatePaymentUseCase(PaymentGateway paymentGateway, WebhookPaymentService webhookPaymentService) {
        this.paymentGateway = paymentGateway;
        this.webhookPaymentService = webhookPaymentService;
    }

    @Override
    public CreatePaymentOutput execute(CreatePaymentCommand command) {
        final var orderID = command.orderID();
        final var clientID = command.clientID();
        final var amount = command.amount();

        final var notification = Notification.create();

        final var payment = notification.validate(() -> Payment.newPayment(orderID, clientID, amount));

        if (notification.hasErrors()) {
            throw NotificationException.with(notification.getErrors());
        }

        processPaymentWithWebhook(payment);

        return CreatePaymentOutput.from(this.paymentGateway.create(payment));
    }

    private void processPaymentWithWebhook(Payment payment) {
        try {
            ResponseEntity<String> response = webhookPaymentService.processPayment(payment);

            if (response.getStatusCode().is2xxSuccessful()) {
                payment.updateStatus(PaymentStatus.APPROVED);
            } else {
                payment.updateStatus(PaymentStatus.FAILED);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o pagamento via webhook", e);
        }
    }
}