package com.fiap.tech.payment.infra.payment.persistence;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fiap.tech.payment.domain.payment.Payment;
import com.fiap.tech.payment.domain.payment.PaymentID;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    Optional<Payment> findByPaymentID(PaymentID paymentID);
}
