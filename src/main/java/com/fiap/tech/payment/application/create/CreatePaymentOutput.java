package com.fiap.tech.payment.application.create;

import java.math.BigDecimal;
import java.time.Instant;

import com.fiap.tech.payment.domain.payment.Payment;

public record CreatePaymentOutput(String paymentId, String orderID, String clientID, BigDecimal amount, Instant createdAt, String paymentStatus) {

    public static CreatePaymentOutput from(final Payment payment){
        return new CreatePaymentOutput(payment.getId().getValue(), payment.getOrderID(), payment.getClientID(), payment.getAmount(), payment.getCreatedAt(), payment.getPaymentStatus().getValue());
    }
}
