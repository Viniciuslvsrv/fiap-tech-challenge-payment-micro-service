package com.fiap.tech.payment.infra.configuration.amqp;

import com.fiap.tech.payment.infra.annotations.PaymentRequestQueue;
import com.fiap.tech.payment.infra.annotations.PaymentReponseQueue;
import com.fiap.tech.payment.infra.configuration.properties.amqp.QueueProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {

    @Bean
    @PaymentReponseQueue
    @ConfigurationProperties("amqp.queues.payment-response")
    public QueueProperties paymentResponseQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @PaymentRequestQueue
    @ConfigurationProperties("amqp.queues.payment-request")
    public QueueProperties paymentRequestQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @PaymentReponseQueue
    public DirectExchange paymentResponseExchange(@PaymentReponseQueue QueueProperties props) {
        return new DirectExchange(props.getExchange());
    }

    @Bean
    @PaymentReponseQueue
    public Queue paymentResponseQueue(@PaymentReponseQueue QueueProperties props) {
        return new Queue(props.getQueue());
    }

    @Bean
    public Binding paymentResponseBinding(
            @PaymentReponseQueue DirectExchange exchange,
            @PaymentReponseQueue Queue queue,
            @PaymentReponseQueue QueueProperties props) {
        return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
    }

    @Bean
    @PaymentRequestQueue
    public DirectExchange paymentRequestExchange(@PaymentRequestQueue QueueProperties props) {
        return new DirectExchange(props.getExchange());
    }

    @Bean
    @PaymentRequestQueue
    public Queue paymentRequestQueue(@PaymentRequestQueue QueueProperties props) {
        return new Queue(props.getQueue());
    }

    @Bean
    public Binding paymentRequestBinding(
            @PaymentRequestQueue DirectExchange exchange,
            @PaymentRequestQueue Queue queue,
            @PaymentRequestQueue QueueProperties props) {
        return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
    }
}
