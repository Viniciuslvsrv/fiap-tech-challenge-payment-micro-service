package com.fiap.tech.payment.infra.amqp;

import com.fiap.tech.payment.application.create.CreatePaymentCommand;
import com.fiap.tech.payment.application.create.DefaultCreatePaymentUseCase;
import com.fiap.tech.payment.infra.configuration.json.Json;
import com.fiap.tech.payment.infra.payment.model.RabbitPaymentResquest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class PaymentEventListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private DefaultCreatePaymentUseCase defaultCreatePaymentUseCase;

    @Value("${amqp.queues.payment-request.queue}")
    private String paymentRequestQueue;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenValidPaymentRequest_whenCallsListener_shouldCallUseCase() throws InterruptedException {
        // Given
        final var expectedClientId = "client-123";
        final var expectedOrderId = "order-456";
        final var expectedAmount = BigDecimal.TEN;

        final var message = "{\"order_id\":\"" + expectedOrderId + "\",\"client_id\":\"" + expectedClientId + "\",\"amount\":" + expectedAmount + "}";

        final var dto = Json.readValue(message, RabbitPaymentResquest.class);
        final var expectedMessage = Json.writeValueAsString(dto);

        // When
        this.rabbitTemplate.convertAndSend(paymentRequestQueue, expectedMessage);

        // Then
        final var invocationData = harness.getNextInvocationDataFor(PaymentEventListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData, "O listener não foi invocado");
        Assertions.assertNotNull(invocationData.getArguments(), "Nenhum argumento foi passado ao listener");
        final var actualMessage = (String) invocationData.getArguments()[0];
        Assertions.assertEquals(expectedMessage, actualMessage, "A mensagem recebida não é a esperada");

        final ArgumentCaptor<CreatePaymentCommand> cmdCaptor = ArgumentCaptor.forClass(CreatePaymentCommand.class);
        verify(defaultCreatePaymentUseCase).execute(cmdCaptor.capture());

        final var actualCommand = cmdCaptor.getValue();
        
        Assertions.assertEquals(expectedClientId, actualCommand.clientID(), "O clientID não confere");
        Assertions.assertEquals(expectedOrderId, actualCommand.orderID(), "O orderID não confere");
        Assertions.assertEquals(expectedAmount, actualCommand.amount(), "O amount não confere");
    }
}
