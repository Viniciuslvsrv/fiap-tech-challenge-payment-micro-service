package com.fiap.tech.payment.application.create;

import com.fiap.tech.payment.domain.payment.Payment;

public record CreatePaymentOutput(String paymentId) {

    public static CreatePaymentOutput from (final Payment payment){
        return new CreatePaymentOutput(payment.getId().getValue());
    }

    public static CreatePaymentOutput from (final String anId){
        return new CreatePaymentOutput(anId);
    }
}
