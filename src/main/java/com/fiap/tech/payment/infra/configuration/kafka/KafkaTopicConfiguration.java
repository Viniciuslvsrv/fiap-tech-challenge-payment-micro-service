package com.fiap.tech.payment.infra.configuration.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfiguration {
    
    @Value(value = "${kafka.topic.payment-request}") String requestTopic;
    @Value(value = "${kafka.topic.payment-response}") String responseTopic;
    @Value(value = "${spring.kafka.bootstrap-servers}") String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
    
    @Bean
    public NewTopic requestTopic() {
        return new NewTopic(requestTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic responseTopic() {
        return new NewTopic(responseTopic, 1, (short) 1);
    }
}
