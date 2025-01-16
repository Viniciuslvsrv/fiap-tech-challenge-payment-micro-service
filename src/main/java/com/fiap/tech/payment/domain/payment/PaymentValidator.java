package com.fiap.tech.payment.domain.payment;

import java.math.BigDecimal;

import com.fiap.tech.payment.domain.validation.ValidationHandler;
import com.fiap.tech.payment.domain.validation.Validator;
import com.fiap.tech.payment.domain.validation.Error;

public class PaymentValidator extends Validator {

    private final Payment payment;

    public PaymentValidator(Payment payment, ValidationHandler aHandler) {
        super(aHandler);
        this.payment = payment;
    }

    @Override
    public void validate() {

        final var orderID = payment.getOrderID();
        final var clientID = payment.getClientID();
        final var amount = payment.getAmount();
        final var paymentStatus = payment.getPaymentStatus();

        if (orderID == null || orderID.isEmpty()) {
            getHandler().append(new Error("OrderID cannot be null or empty."));
        }
        if (clientID == null || clientID.isEmpty()) {
            getHandler().append(new Error("ClientID cannot be null or empty."));
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            getHandler().append(new Error("Amount must be greater than zero."));
        }
        if (paymentStatus == null) {
            getHandler().append(new Error("Payment status cannot be null."));
        }
    }
}
