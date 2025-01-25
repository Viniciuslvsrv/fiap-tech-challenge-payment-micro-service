package com.fiap.tech.payment.infra.payment.persistence;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fiap.tech.payment.domain.payment.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    @Query("{ '_id.value': ?0 }")
    Optional<Payment> findByPaymentID(String paymentID);
}
