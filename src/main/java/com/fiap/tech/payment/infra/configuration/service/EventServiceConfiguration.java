package com.fiap.tech.payment.infra.configuration.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fiap.tech.payment.infra.service.EventService;
import com.fiap.tech.payment.infra.service.impl.KafkaEventService;

@Configuration
public class EventServiceConfiguration {

    @Bean
    EventService kafkaEventService() {
        return new KafkaEventService();
    }
}
