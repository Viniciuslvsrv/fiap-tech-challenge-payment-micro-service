package com.fiap.tech.payment.infra.service.impl;

import com.fiap.tech.payment.domain.payment.Payment;
import com.fiap.tech.payment.domain.payment.PaymentID;
import com.fiap.tech.payment.domain.payment.PaymentStatus;
import com.fiap.tech.payment.infra.configuration.json.Json;
import com.fiap.tech.payment.infra.service.EventService;
import com.fiap.tech.payment.util.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PaymentEventServiceTest {

    private static final String EXCHANGE = "test-exchange";
    private static final String ROUTING_KEY = "test-routing-key";

    @Mock
    private RabbitOperations rabbitOperations;

    private EventService paymentEventService;

    @BeforeEach
    public void setUp() {
        paymentEventService = new PaymentEventService(EXCHANGE, ROUTING_KEY, rabbitOperations);
    }

    @Test
    public void sendShouldCallConvertAndSendWithExpectedMessage() {
        // Given
        final var expectedId = TestUtil.randomID();
        PaymentID paymentID = PaymentID.from(expectedId);
        Payment payment = TestUtil.createMockPayment(paymentID, PaymentStatus.APPROVED);
        
        String expectedMessage = Json.writeValueAsString(payment);

        // When
        paymentEventService.send(payment);

        // Then
        verify(rabbitOperations, times(1)).convertAndSend(EXCHANGE, ROUTING_KEY, expectedMessage);
    }

    @Test
    public void sendShouldHandleExceptionGracefully() {
        // Given
        final var expectedId = TestUtil.randomID();
        PaymentID paymentID = PaymentID.from(expectedId);
        Payment payment = TestUtil.createMockPayment(paymentID, PaymentStatus.APPROVED);
        String expectedMessage = Json.writeValueAsString(payment);

        doThrow(new RuntimeException("Erro simulado"))
            .when(rabbitOperations)
            .convertAndSend(EXCHANGE, ROUTING_KEY, expectedMessage);

        // When && Then
        assertDoesNotThrow(() -> paymentEventService.send(payment));
        verify(rabbitOperations, times(1)).convertAndSend(EXCHANGE, ROUTING_KEY, expectedMessage);
    }
}
