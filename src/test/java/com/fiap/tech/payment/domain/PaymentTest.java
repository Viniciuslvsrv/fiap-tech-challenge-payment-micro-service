package com.fiap.tech.payment.domain;

import com.fiap.tech.payment.domain.exception.DomainException;
import com.fiap.tech.payment.domain.exception.NotificationException;
import com.fiap.tech.payment.domain.payment.Payment;
import com.fiap.tech.payment.domain.payment.PaymentCreated;
import com.fiap.tech.payment.domain.payment.PaymentID;
import com.fiap.tech.payment.domain.payment.PaymentStatus;
import com.fiap.tech.payment.domain.payment.PaymentValidator;
import com.fiap.tech.payment.domain.validation.handler.Notification;
import com.fiap.tech.payment.domain.validation.handler.ThrowsValidationHandler;
import com.fiap.tech.payment.domain.validation.Error;
import com.fiap.tech.payment.util.TestUtil;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentTest {

    @Test
    public void givenValidParams_whenCallNewPayment_thenInstantiateAPayment() {
        final var expectedOrderID = "12345";
        final var expectedClientID = "67890";
        final var expectedAmount = BigDecimal.valueOf(100.50);

        final var actualPayment = Payment.newPayment(expectedOrderID, expectedClientID, expectedAmount);

        assertNotNull(actualPayment);
        assertNotNull(actualPayment.getId());
        assertEquals(expectedOrderID, actualPayment.getOrderID());
        assertEquals(expectedClientID, actualPayment.getClientID());
        assertEquals(expectedAmount, actualPayment.getAmount());
        assertNotNull(actualPayment.getCreatedAt());
        assertEquals(PaymentStatus.UNIDENTIFIED, actualPayment.getPaymentStatus());
    }

    @Test
    public void givenInvalidNullOrderID_whenCallNewPaymentAndValidate_thenShouldReceiveError() {
        final String expectedOrderID = null;
        final var expectedClientID = "67890";
        final var expectedAmount = BigDecimal.valueOf(100.50);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "OrderID cannot be null or empty.";

        final var actualPayment = Payment.newPayment(expectedOrderID, expectedClientID, expectedAmount);

        final var actualException = assertThrows(DomainException.class, () -> actualPayment.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertTrue(actualException.getErrors().stream()
            .anyMatch(error -> error.message().contains(expectedErrorMessage)));
    }

    @Test
    public void givenInvalidNegativeAmount_whenCallNewPaymentAndValidate_thenShouldReceiveError() {
        final var expectedOrderID = "12345";
        final var expectedClientID = "67890";
        final var expectedAmount = BigDecimal.valueOf(-10.0);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Amount must be greater than zero.";

        final var actualPayment = Payment.newPayment(expectedOrderID, expectedClientID, expectedAmount);

        final var actualException = assertThrows(DomainException.class, () -> actualPayment.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertTrue(actualException.getErrors().stream()
            .anyMatch(error -> error.message().contains(expectedErrorMessage)));
    }

    @Test
    public void givenValidPayment_whenCallUpdateStatus_thenReturnPaymentWithUpdatedStatus() {
        final var expectedOrderID = "12345";
        final var expectedClientID = "67890";
        final var expectedAmount = BigDecimal.valueOf(100.50);
        final var expectedStatus = PaymentStatus.APPROVED;

        final var actualPayment = Payment.newPayment(expectedOrderID, expectedClientID, expectedAmount);

        final var updatedPayment = actualPayment.updateStatus(expectedStatus);

        assertNotNull(updatedPayment);
        assertEquals(expectedStatus, updatedPayment.getPaymentStatus());
        assertEquals(actualPayment.getId(), updatedPayment.getId());
    }

    @Test
    public void givenValidPayment_whenCallWith_thenShouldReturnIdenticalPayment() {
        final var expectedOrderID = "12345";
        final var expectedClientID = "67890";
        final var expectedAmount = BigDecimal.valueOf(100.50);
        final var expectedCreatedAt = Instant.now();
        final var expectedStatus = PaymentStatus.UNIDENTIFIED;

        final var originalPayment = Payment.with(PaymentID.unique(), expectedOrderID, expectedClientID, expectedAmount, expectedCreatedAt, expectedStatus);

        final var newPayment = Payment.with(originalPayment);

        assertNotNull(newPayment);
        assertEquals(originalPayment.getId(), newPayment.getId());
        assertEquals(originalPayment.getOrderID(), newPayment.getOrderID());
        assertEquals(originalPayment.getClientID(), newPayment.getClientID());
        assertEquals(originalPayment.getAmount(), newPayment.getAmount());
        assertEquals(originalPayment.getCreatedAt(), newPayment.getCreatedAt());
        assertEquals(originalPayment.getPaymentStatus(), newPayment.getPaymentStatus());
    }

    @Test
    public void givenValidParams_whenCallToString_thenReturnCorrectStringRepresentation() {
        final var expectedOrderID = "12345";
        final var expectedClientID = "67890";
        final var expectedAmount = BigDecimal.valueOf(100.50);
        final var expectedCreatedAt = Instant.now();
        final var expectedStatus = PaymentStatus.UNIDENTIFIED;

        final var actualPayment = Payment.with(PaymentID.unique(), expectedOrderID, expectedClientID, expectedAmount, expectedCreatedAt, expectedStatus);

        final var expectedString = "Payment{" +
                "paymentID=" + actualPayment.getId() + '\'' +
                ", orderID='" + expectedOrderID + '\'' +
                ", clientID='" + expectedClientID + '\'' +
                ", amount=" + expectedAmount +
                ", createdAt=" + expectedCreatedAt +
                ", paymentStatus=" + expectedStatus +
                '}';

        assertEquals(expectedString, actualPayment.toString());
    }

        @Test
    public void givenValidPayment_whenCreatePaymentCreatedEvent_thenShouldInstantiateCorrectly() {
        final var payment = Payment.newPayment("12345", "67890", BigDecimal.valueOf(100.50));
        final var event = new PaymentCreated(payment);

        assertNotNull(event);
        assertEquals(payment.getId().getValue(), event.paymentID());
        assertEquals(payment.getOrderID(), event.orderID());
        assertEquals(payment.getClientID(), event.clientID());
        assertEquals(payment.getAmount(), event.amount());
        assertEquals(payment.getCreatedAt(), event.createdAt());
        assertEquals(payment.getPaymentStatus(), event.paymentStatus());
        assertNotNull(event.occurredOn());
    }

    @Test
    public void givenValidStatus_whenUsingPaymentStatus_thenShouldReturnCorrectValues() {
        assertEquals("Approved", PaymentStatus.APPROVED.getValue());
        assertEquals("Failed", PaymentStatus.FAILED.getValue());
        assertEquals("Unidentified", PaymentStatus.UNIDENTIFIED.getValue());
    }

    @Test
    public void givenValidString_whenCallValueOf_thenShouldReturnCorrectPaymentStatus() {
        final var status = PaymentStatus.valueOf("CustomStatus");
        assertEquals("CustomStatus", status.getValue());
    }

    @Test
    public void givenTwoSamePaymentStatus_whenCompare_thenShouldBeEqual() {
        final var status1 = PaymentStatus.valueOf("Approved");
        final var status2 = PaymentStatus.valueOf("Approved");

        assertEquals(status1, status2);
        assertEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    public void givenInvalidPayment_whenValidate_thenShouldReturnErrors() {
        // Arrange
        String paymentIDString = TestUtil.randomID();

        PaymentID paymentID = PaymentID.from(paymentIDString);

        final var invalidPayment = Payment.with(paymentID, null, null, BigDecimal.valueOf(-10), Instant.now(), null);
        final var validationHandler = new ThrowsValidationHandler();

        final var exception = assertThrows(DomainException.class, () -> {
            new PaymentValidator(invalidPayment, validationHandler).validate();
        });

        assertTrue(exception.getErrors().stream().allMatch(e -> exception.getErrors().contains(e)));
    }

    @Test
    void testNotificationException_withNotification() {
        // Arrange
        String expectedMessage = "Notification error occurred";
        Notification notification = Notification.create()
            .append(new Error("First error"))
            .append(new Error("Second error"));

        // Act
        NotificationException exception = new NotificationException(expectedMessage, notification);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertNotNull(exception.getErrors());
        assertEquals(2, exception.getErrors().size());
        assertEquals("First error", exception.getErrors().stream().findFirst().get().message());
        assertEquals("Second error", exception.getErrors().stream().skip(1).findFirst().get().message());
    }

    @Test
    void testNotificationException_emptyNotification() {
        // Arrange
        String expectedMessage = "Empty notification";
        Notification notification = Notification.create();

        // Act
        NotificationException exception = new NotificationException(expectedMessage, notification);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertNotNull(exception.getErrors());
        assertTrue(exception.getErrors().isEmpty());
    }

    @Test
    void testNotificationException_createNotification() {
        // Arrange
        String expectedMessage = "Empty notification";
        Notification notification = Notification.create(new Throwable(expectedMessage));

        // Act
        NotificationException exception = new NotificationException(expectedMessage, notification);

        // Assert
        assertEquals(notification.getErrors().get(0).message(), expectedMessage);
        assertNotNull(exception.getErrors());
    }

    @Test
    void testNotificationException_emptyThrowsValidationHandler() {
        // Arrange
        String expectedMessage = "Empty validation handler";
        ThrowsValidationHandler validationHandler = new ThrowsValidationHandler();

        // Act
        NotificationException exception = new NotificationException(expectedMessage, validationHandler);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
        assertNotNull(exception.getErrors());
        assertTrue(exception.getErrors().isEmpty());
    }
}
