package com.fiap.tech.payment.infra.payment;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.tech.payment.domain.payment.Payment;
import com.fiap.tech.payment.domain.payment.PaymentCreated;
import com.fiap.tech.payment.domain.payment.PaymentGateway;
import com.fiap.tech.payment.domain.payment.PaymentID;
import com.fiap.tech.payment.infra.payment.persistence.PaymentRepository;
import com.fiap.tech.payment.infra.service.EventService;

import java.util.Objects;

@Component
public class DefaultPaymentGateway implements PaymentGateway {

    private final EventService eventService;
    private final PaymentRepository paymentRepository;

    public DefaultPaymentGateway(final EventService eventService, final PaymentRepository paymentRepository) {
        this.eventService = Objects.requireNonNull(eventService);
        this.paymentRepository = Objects.requireNonNull(paymentRepository);
    }

    @Override
    @Transactional
    public Payment create(Payment payment) {
        return save(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findById(PaymentID paymentID) {
        return this.paymentRepository.findByPaymentID(paymentID);
    }

    private Payment save(final Payment payment) {
        final var result = this.paymentRepository.save(payment);

        payment.registerEvent(new PaymentCreated(result));

        payment.publishDomainEvents(this.eventService::send);

        return result;
    }
}