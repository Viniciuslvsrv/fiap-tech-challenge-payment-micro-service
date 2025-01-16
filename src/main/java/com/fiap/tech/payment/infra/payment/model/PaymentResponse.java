package com.fiap.tech.payment.infra.payment.model;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.tech.payment.application.retrieve.get.GetPaymentOutput;

public record PaymentResponse(
    @JsonProperty("payment_id") String paymentID,
    @JsonProperty("order_id") String orderID, 
    @JsonProperty("client_id") String clientID,
    @JsonProperty("amount") BigDecimal amount,
    @JsonProperty("createdAt") Instant createdAt,
    @JsonProperty("payment_status") String paymentStatus
) {

    public static PaymentResponse from(final GetPaymentOutput output) {
        return new PaymentResponse(
                    output.paymentID(), 
                    output.orderID(), 
                    output.clientID(), 
                    output.amount(), 
                    output.createdAt(), 
                    output.paymentStatus());
    }
}
