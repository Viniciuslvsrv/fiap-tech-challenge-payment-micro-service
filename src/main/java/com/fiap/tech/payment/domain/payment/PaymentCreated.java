package com.fiap.tech.payment.domain.payment;

import java.math.BigDecimal;
import java.time.Instant;

import com.fiap.tech.payment.domain.event.DomainEvent;
import com.fiap.tech.payment.domain.util.InstantUtil;

public record PaymentCreated(String paymentID, String orderID, String clientID, BigDecimal amount, Instant createdAt, PaymentStatus paymentStatus, Instant occurredOn) implements DomainEvent {

    public  PaymentCreated(final Payment payment) {
       this(payment.getId().getValue(), payment.getOrderID(), payment.getClientID(), payment.getAmount(), payment.getCreatedAt(), payment.getPaymentStatus(), InstantUtil.now());
    }
}
