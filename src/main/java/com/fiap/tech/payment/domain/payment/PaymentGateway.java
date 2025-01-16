package com.fiap.tech.payment.domain.payment;

import java.util.Optional;

public interface PaymentGateway {
    Payment create(Payment payment);

    Optional<Payment> findById(PaymentID paymentID);
}
