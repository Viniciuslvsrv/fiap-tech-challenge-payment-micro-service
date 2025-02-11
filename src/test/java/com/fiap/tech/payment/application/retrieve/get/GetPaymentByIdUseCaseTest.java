package com.fiap.tech.payment.application.retrieve.get;

import com.fiap.tech.payment.application.util.IDNotFoundUtils;
import com.fiap.tech.payment.domain.exception.DomainException;
import com.fiap.tech.payment.domain.exception.NotFoundException;
import com.fiap.tech.payment.domain.payment.Payment;
import com.fiap.tech.payment.domain.payment.PaymentGateway;
import com.fiap.tech.payment.domain.payment.PaymentID;
import com.fiap.tech.payment.domain.payment.PaymentStatus;
import com.fiap.tech.payment.util.TestUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPaymentByIdUseCaseTest {

    @Mock
    private PaymentGateway paymentGateway;

    @InjectMocks
    private DefaultGetPaymentByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void givenValidId_whenExecute_thenReturnsPayment() {
        // Arrange
        String paymentIDString = TestUtil.randomID();

        PaymentID paymentID = PaymentID.from(paymentIDString);
        Payment payment = TestUtil.createMockPayment(paymentID, PaymentStatus.UNIDENTIFIED);

        when(paymentGateway.findById(paymentID)).thenReturn(Optional.of(payment));

        // Act
        GetPaymentOutput result = useCase.execute(paymentIDString);

        // Assert
        assertNotNull(result);
        verify(paymentGateway, times(1)).findById(paymentID);
    }

    @Test
    void givenInvalidId_whenExecute_thenThrowsException() {
        // Arrange
        String paymentIDString = TestUtil.randomID();

        PaymentID paymentID = PaymentID.from(paymentIDString);

        when(paymentGateway.findById(paymentID)).thenReturn(Optional.empty());

        // Act 
        Exception exception = assertThrows(RuntimeException.class, () -> useCase.execute(paymentIDString));

        // Assert
        assertTrue(exception.getMessage().contains(paymentIDString));
        verify(paymentGateway, times(1)).findById(paymentID);
    }

    @Test
    void givenIdentifierAndAggregate_whenCallingNotFound_thenReturnsSupplier() {
        // Arrange
        String paymentIDString = TestUtil.randomID();

        PaymentID paymentID = PaymentID.from(paymentIDString);

        // Act
        Supplier<DomainException> supplier = IDNotFoundUtils.notFound(paymentID, Payment.class);
        DomainException exception = supplier.get();

        // Assert
        assertNotNull(exception);
        assertTrue(exception instanceof NotFoundException);
        assertTrue(exception.getMessage().contains(paymentIDString));
    }

    @Test
    void shouldInstantiateIDNotFoundUtils() {
        // Act
        IDNotFoundUtils idNotFoundUtils = new IDNotFoundUtils();

        // Assert
        assertNotNull(idNotFoundUtils);
    }
}