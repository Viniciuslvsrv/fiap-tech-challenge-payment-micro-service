package com.fiap.tech.payment.infra.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fiap.tech.payment.application.create.CreatePaymentCommand;
import com.fiap.tech.payment.application.create.DefaultCreatePaymentUseCase;
import com.fiap.tech.payment.infra.configuration.json.Json;
import com.fiap.tech.payment.infra.payment.model.KafkaPaymentResquest;

@Service
public class KafkaEventListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventListener.class);

    private final DefaultCreatePaymentUseCase defaultCreatePaymentUseCase;
    
    public KafkaEventListener(DefaultCreatePaymentUseCase defaultCreatePaymentUseCase) {
        this.defaultCreatePaymentUseCase = defaultCreatePaymentUseCase;
    }

    @KafkaListener(
        topics = "${kafka.topic.payment-request}",
        groupId = "${kafka.topic.payment-group}"
    )
    public void consumePaymentEvent(String message) {
        final var aResult = Json.readValue(message, KafkaPaymentResquest.class);
        
        if (aResult instanceof KafkaPaymentResquest) {
            log.info("[message:payment.listener.income] [status:completed] [payload:{}]", message);

            KafkaPaymentResquest dto = (KafkaPaymentResquest) aResult;
    
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
