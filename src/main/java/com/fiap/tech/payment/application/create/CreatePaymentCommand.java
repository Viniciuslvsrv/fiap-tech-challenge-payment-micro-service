package com.fiap.tech.payment.application.create;

import java.math.BigDecimal;

public record CreatePaymentCommand(String clientID, String orderID, BigDecimal amount) {
    
    public static CreatePaymentCommand with(String clientID, String orderID, BigDecimal amount) {
        return new CreatePaymentCommand(clientID, orderID, amount);
    }
}