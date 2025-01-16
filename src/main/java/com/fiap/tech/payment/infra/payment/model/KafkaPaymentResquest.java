package com.fiap.tech.payment.infra.payment.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KafkaPaymentResquest(
    @JsonProperty("payment_id") String paymentID, 
    @JsonProperty("order_id") String orderID,
    @JsonProperty("client_id") String clientID,
    @JsonProperty("amount") BigDecimal amount
) {

}
