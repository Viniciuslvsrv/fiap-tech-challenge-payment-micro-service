package com.fiap.tech.payment.infra.configuration.service;

import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fiap.tech.payment.infra.annotations.PaymentReponseQueue;
import com.fiap.tech.payment.infra.configuration.properties.amqp.QueueProperties;
import com.fiap.tech.payment.infra.service.EventService;
import com.fiap.tech.payment.infra.service.impl.PaymentEventService;

@Configuration
public class EventServiceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EventService paymentEventService(
        @PaymentReponseQueue final QueueProperties props,
        final RabbitOperations ops
    ) {
        return new PaymentEventService(props.getExchange(), props.getRoutingKey(), ops);
    }
}
