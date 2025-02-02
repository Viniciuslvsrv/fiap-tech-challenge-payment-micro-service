package com.fiap.tech.payment.application.create;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.fiap.tech.payment.domain.exception.DomainException;
import com.fiap.tech.payment.domain.exception.NotificationException;
import com.fiap.tech.payment.domain.payment.*;
import com.fiap.tech.payment.domain.validation.Error;
import com.fiap.tech.payment.infra.service.impl.WebhookPaymentService;
import com.fiap.tech.payment.util.TestUtil;

class CreatePaymentUseCaseTest {

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private WebhookPaymentService webhookPaymentService;

    @InjectMocks
    private DefaultCreatePaymentUseCase defaultCreatePaymentUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldProcessPaymentSuccessfully_whenCommandIsValid() {
        try (MockedStatic<Payment> paymentClass = mockStatic(Payment.class)) {
            // Arrange
            String orderID = TestUtil.randomID();
            String clientID = TestUtil.randomID();

            var command = new CreatePaymentCommand(clientID, orderID, BigDecimal.TEN);

            Payment payment = TestUtil.createMockPayment(command.orderID(), command.clientID(), command.amount(), PaymentStatus.UNIDENTIFIED);
            paymentClass.when(() -> Payment.newPayment(anyString(), anyString(), any(BigDecimal.class))).thenReturn(payment);

            when(webhookPaymentService.processPayment(any(Payment.class)))
                .thenAnswer(invocation -> {
                    Payment paymentArg = invocation.getArgument(0);
                    paymentArg.setPaymentStatus(PaymentStatus.APPROVED);
                    return ResponseEntity.ok("Webhook processed successfully");
                });

            when(paymentGateway.create(any(Payment.class))).thenReturn(payment);

            // Act
            var output = defaultCreatePaymentUseCase.execute(command);

            // Assert
            assertNotNull(output);
            assertEquals(PaymentStatus.APPROVED.getValue(), output.paymentStatus());
            verify(paymentGateway).create(any(Payment.class));
            verify(webhookPaymentService).processPayment(any(Payment.class));
        }
    }

    @Test
    void shouldUpdateStatusToFailed_whenWebhookFails() {
        try (MockedStatic<Payment> paymentClass = mockStatic(Payment.class)) {
            // Arrange
            String orderID = TestUtil.randomID();
            String clientID = TestUtil.randomID();

            var command = new CreatePaymentCommand(clientID, orderID, BigDecimal.TEN);

            Payment payment = TestUtil.createMockPayment(command.orderID(), command.clientID(), command.amount(), PaymentStatus.UNIDENTIFIED);
            paymentClass.when(() -> Payment.newPayment(anyString(), anyString(), any(BigDecimal.class))).thenReturn(payment);

            when(webhookPaymentService.processPayment(any(Payment.class)))
                .thenAnswer(invocation -> {
                    Payment paymentArg = invocation.getArgument(0);
                    paymentArg.setPaymentStatus(PaymentStatus.FAILED);
                    return ResponseEntity.status(500).body("Webhook failed");
                });

            when(paymentGateway.create(any(Payment.class))).thenReturn(payment);

            // Act
            var output = defaultCreatePaymentUseCase.execute(command);

            // Assert
            assertNotNull(output);
            assertEquals(PaymentStatus.FAILED.getValue(), output.paymentStatus());
            verify(webhookPaymentService).processPayment(any(Payment.class));
            verify(paymentGateway).create(any(Payment.class));
        }
    }

    @Test
    void shouldThrowRuntimeException_whenWebhookThrowsException() {
        try (MockedStatic<Payment> paymentClass = mockStatic(Payment.class)) {
            // Arrange
            String orderID = TestUtil.randomID();
            String clientID = TestUtil.randomID();

            var command = new CreatePaymentCommand(clientID, orderID, BigDecimal.TEN);

            Payment payment = TestUtil.createMockPayment(command.orderID(), command.clientID(), command.amount(), PaymentStatus.UNIDENTIFIED);

            paymentClass.when(() -> Payment.newPayment(anyString(), anyString(), any(BigDecimal.class)))
                .thenReturn(payment);

            when(webhookPaymentService.processPayment(any(Payment.class)))
                .thenThrow(new RuntimeException("Erro ao processar o pagamento via webhook"));

            // Act
            var exception = assertThrows(RuntimeException.class, () -> defaultCreatePaymentUseCase.execute(command));

            // Assert
            assertTrue(exception.getMessage().contains("Erro ao processar o pagamento via webhook"));
            verify(webhookPaymentService).processPayment(any(Payment.class));
        }
    }

    @Test
    void shouldThrowNotificationException_whenMultipleValidationErrorsOccur() {
        try (MockedStatic<Payment> paymentClass = mockStatic(Payment.class)) {
            // Arrange
            String orderID = "";
            String clientID = "";

            var command = new CreatePaymentCommand(clientID, orderID, BigDecimal.ZERO);
            var errors = List.of(
                new Error("OrderID cannot be null or empty."),
                new Error("ClientID cannot be null or empty."),
                new Error("Amount must be greater than zero."));

            paymentClass.when(() -> Payment.newPayment(anyString(), anyString(), any(BigDecimal.class)))
                    .thenThrow(NotificationException.with(errors));
            
            // Act
            var exception = assertThrows(DomainException.class, () -> defaultCreatePaymentUseCase.execute(command));

            // Assert
            assertTrue(errors.stream().allMatch(e -> exception.getErrors().contains(e)));
            verify(paymentGateway, never()).create(any(Payment.class));
        }
    }

    @Test
    void shouldThrowNotificationException_whenValidationErrorOccurs() {
        try (MockedStatic<Payment> paymentClass = mockStatic(Payment.class)) {
            // Arrange
            String orderID = "";
            String clientID = TestUtil.randomID();

            var command = new CreatePaymentCommand(clientID, orderID, BigDecimal.TEN);

            var errors = new Error("OrderID cannot be null or empty.");
            
            paymentClass.when(() -> Payment.newPayment(anyString(), anyString(), any(BigDecimal.class)))
                .thenThrow(NotificationException.with(errors));
            
            // Act
            var exception = assertThrows(DomainException.class, () -> defaultCreatePaymentUseCase.execute(command));

            // Assert
            assertTrue(exception.getErrors().stream()
                    .anyMatch(error -> error.message().contains("OrderID cannot be null or empty.")));
            verify(paymentGateway, never()).create(any(Payment.class));
        }
    }

    @Test
    void shouldCreateCommandWithValidValues() {
        // Arrange
        String clientID = TestUtil.randomID();
        String orderID = TestUtil.randomID();
        BigDecimal amount = BigDecimal.TEN;

        // Act
        CreatePaymentCommand command = CreatePaymentCommand.with(clientID, orderID, amount);

        // Assert
        assertNotNull(command);
        assertEquals(clientID, command.clientID());
        assertEquals(orderID, command.orderID());
        assertEquals(amount, command.amount());
    }
}