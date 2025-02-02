package com.fiap.tech.payment.infra.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fiap.tech.payment.application.create.CreatePaymentCommand;
import com.fiap.tech.payment.application.create.DefaultCreatePaymentUseCase;
import com.fiap.tech.payment.infra.configuration.json.Json;
import com.fiap.tech.payment.infra.payment.model.RabbitPaymentResquest;

@Service
public class PaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    static final String LISTENER_ID = "PaymentEventListener";

    private final DefaultCreatePaymentUseCase defaultCreatePaymentUseCase;
    
    public PaymentEventListener(DefaultCreatePaymentUseCase defaultCreatePaymentUseCase) {
        this.defaultCreatePaymentUseCase = defaultCreatePaymentUseCase;
    }

    @RabbitListener(
        id = LISTENER_ID,
        queues = "${amqp.queues.payment-request.queue}"
    )
    public void consumePaymentEvent(String message) {
        final var aResult = Json.readValue(message, RabbitPaymentResquest.class);
        
        if (aResult instanceof RabbitPaymentResquest) {
            log.info("[message:payment.listener.income] [status:completed] [payload:{}]", message);

            RabbitPaymentResquest dto = (RabbitPaymentResquest) aResult;
    
            final var aCmd = new CreatePaymentCommand(
                dto.clientID(),
                dto.orderID(),
                dto.amount()
            );
    
            defaultCreatePaymentUseCase.execute(aCmd);
        } else {
            log.error("[message:payment.listener.income] [status:error] [payload:{}]", message);
        }
    }
    
}
