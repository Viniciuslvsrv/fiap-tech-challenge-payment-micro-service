package com.fiap.tech.payment.infra.service.impl;

import com.fiap.tech.payment.infra.service.EventService;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

public class KafkaEventService implements EventService {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.payment-response}") String topic;

    @Override
    public void send(Object event) {
        String payload = event.toString();

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, payload);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send message: " + ex.getMessage());
            } else {
                log.info("Message sent successfully to topic " + topic + " with payload: " + payload);
            }
        });
    }
}
