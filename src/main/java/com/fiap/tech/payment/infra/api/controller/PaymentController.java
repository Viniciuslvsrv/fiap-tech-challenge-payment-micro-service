package com.fiap.tech.payment.infra.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.tech.payment.application.retrieve.get.GetPaymentByIdUseCase;
import com.fiap.tech.payment.infra.api.PaymentAPI;
import com.fiap.tech.payment.infra.payment.model.PaymentResponse;

@RestController
@RequestMapping(value = "payments")
public class PaymentController implements PaymentAPI {

    private final GetPaymentByIdUseCase getPaymentByIdUseCase;

    public PaymentController(GetPaymentByIdUseCase getPaymentByIdUseCase) {
        this.getPaymentByIdUseCase = getPaymentByIdUseCase;
    }

    @Override
    public PaymentResponse getById(String paymentID) {
        return PaymentResponse.from(getPaymentByIdUseCase.execute(paymentID));
    }
}
