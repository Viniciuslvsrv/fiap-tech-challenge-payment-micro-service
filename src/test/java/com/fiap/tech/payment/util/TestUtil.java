package com.fiap.tech.payment.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.fiap.tech.payment.domain.payment.Payment;
import com.fiap.tech.payment.domain.payment.PaymentID;
import com.fiap.tech.payment.domain.payment.PaymentStatus;

public class TestUtil {

    public static String randomID() {
        return UUID.randomUUID().toString();
    }

    public static Payment createMockPayment(PaymentID paymentID, PaymentStatus status) {
        String orderID = randomID();
        String clientID = randomID();

        return new Payment(paymentID, orderID, clientID, BigDecimal.TEN, Instant.now(), status);
    }

    public static Payment createMockPayment(String orderID, String clientID, BigDecimal amount, PaymentStatus status) {
        return new Payment(PaymentID.unique(), orderID, clientID, amount, Instant.now(), status);
    }
}
