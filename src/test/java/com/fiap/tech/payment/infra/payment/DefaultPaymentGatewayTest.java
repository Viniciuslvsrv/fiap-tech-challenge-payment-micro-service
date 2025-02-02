package com.fiap.tech.payment.infra.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.fiap.tech.payment.domain.payment.Payment;
import com.fiap.tech.payment.domain.payment.PaymentCreated;
import com.fiap.tech.payment.domain.payment.PaymentGateway;
import com.fiap.tech.payment.domain.payment.PaymentID;
import com.fiap.tech.payment.domain.payment.PaymentStatus;
import com.fiap.tech.payment.infra.payment.persistence.PaymentRepository;
import com.fiap.tech.payment.infra.service.EventService;
import com.fiap.tech.payment.util.TestUtil;

@SpringBootTest
class DefaultPaymentGatewayTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private EventService eventService;

    private PaymentGateway paymentGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentGateway = new DefaultPaymentGateway(eventService, paymentRepository);
    }

    @Test
    void createRegistersAndPublishesEvent() {
        final var expectedId = TestUtil.randomID();
        PaymentID paymentID = PaymentID.from(expectedId);
        Payment payment = spy(TestUtil.createMockPayment(paymentID, PaymentStatus.APPROVED));
        
        Payment insertedPayment = spy(TestUtil.createMockPayment(paymentID, PaymentStatus.APPROVED));
        when(paymentRepository.insert(payment)).thenReturn(insertedPayment);

        Payment result = paymentGateway.create(payment);
    
        verify(paymentRepository).insert(payment);
    
        ArgumentCaptor<PaymentCreated> captor = ArgumentCaptor.forClass(PaymentCreated.class);
        verify(payment).registerEvent(captor.capture());
        PaymentCreated event = captor.getValue();
        assertNotNull(event, "O evento PaymentCreated não deveria ser nulo");
        verify(payment).publishDomainEvents(any());
        assertEquals(insertedPayment, result, "O Payment retornado deve ser o Payment inserido");
    }
    

    @Test
    void findById() {
        // Given
        final var expectedId = TestUtil.randomID();
        PaymentID paymentID = PaymentID.from(expectedId);
        Payment payment = TestUtil.createMockPayment(paymentID, PaymentStatus.APPROVED);

        when(paymentRepository.findByPaymentID(expectedId)).thenReturn(Optional.of(payment));

        // When
        Optional<Payment> found = paymentGateway.findById(paymentID);

        // Then
        verify(paymentRepository).findByPaymentID(expectedId);
        assertTrue(found.isPresent(), "O Payment deveria estar presente");
        assertEquals(payment, found.get(), "O Payment retornado deve ser o Payment esperado");
    }
}
