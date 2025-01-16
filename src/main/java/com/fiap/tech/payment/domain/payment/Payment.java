package com.fiap.tech.payment.domain.payment;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fiap.tech.payment.domain.validation.ValidationHandler;
import com.fiap.tech.payment.domain.AggregateRoot;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "payments")
@Getter
@Setter
public class Payment extends AggregateRoot<PaymentID> {

    private PaymentID paymentID;
    private String orderID;
    private String clientID;
    private BigDecimal amount;
    private Instant createdAt;
    private PaymentStatus paymentStatus;

    public Payment(PaymentID paymentID, String orderID, String clientID, BigDecimal amount, Instant createdAt, PaymentStatus paymentStatus) {
        super(paymentID);
        this.orderID = orderID;
        this.clientID = clientID;
        this.amount = amount;
        this.createdAt = createdAt;
        this.paymentStatus = paymentStatus;
    }

    public static Payment newPayment(String orderID, String clientID, BigDecimal amount) {
        final var now = Instant.now();
        final var paymentID = PaymentID.unique();
        final var paymentStatus = PaymentStatus.UNIDENTIFIED;

        return new Payment(paymentID, orderID, clientID, amount, now, paymentStatus);
    }

    public Payment updateStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public static Payment with(PaymentID paymentID, String orderID, String clientID, BigDecimal amount, Instant createAt, PaymentStatus paymentStatus) {
        return new Payment(paymentID, orderID, clientID, amount, createAt, paymentStatus);
    }

    public static Payment with(Payment payment) {
        return with(payment.getId(), payment.getOrderID(), payment.getClientID(), payment.getAmount(), payment.getCreatedAt(), payment.getPaymentStatus());
    }

    @Override
    public void validate(ValidationHandler handler) {
        new PaymentValidator(this, handler).validate();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + getId() + '\'' +
                ", orderID='" + orderID + '\'' +
                ", clientID='" + clientID + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                ", paymentStatus=" + paymentStatus +
                '}';
    }

}