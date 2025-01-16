package com.fiap.tech.payment.application.retrieve.get;

import java.math.BigDecimal;
import java.time.Instant;

import com.fiap.tech.payment.domain.payment.Payment;

public record GetPaymentOutput(String paymentID, String orderID, String clientID, BigDecimal amount, Instant createdAt, String paymentStatus) {

    public static GetPaymentOutput from (Payment payment){
        return new GetPaymentOutput(payment.getId().getValue(), payment.getOrderID(), payment.getClientID(), payment.getAmount(), payment.getCreatedAt(), payment.getPaymentStatus().getValue());
    }
}
