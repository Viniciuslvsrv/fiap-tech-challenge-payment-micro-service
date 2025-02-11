package com.fiap.tech.payment.infra.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.tech.payment.application.retrieve.get.GetPaymentByIdUseCase;
import com.fiap.tech.payment.application.retrieve.get.GetPaymentOutput;
import com.fiap.tech.payment.application.util.IDNotFoundUtils;
import com.fiap.tech.payment.domain.payment.Payment;
import com.fiap.tech.payment.domain.payment.PaymentID;
import com.fiap.tech.payment.domain.payment.PaymentStatus;
import com.fiap.tech.payment.infra.api.controller.PaymentController;
import com.fiap.tech.payment.infra.payment.model.PaymentResponse;
import com.fiap.tech.payment.util.TestUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
public class PaymentAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GetPaymentByIdUseCase getPaymentByIdUseCase;

    @Test
    public void givenAValidId_whenCallsGetPaymentById_shouldReturnPayment() throws Exception {
        // Given
        final var expectedId = TestUtil.randomID();
        final var expectedStatus = "Approved";
        final var expectedAmount = 10;

        PaymentID paymentID = PaymentID.from(expectedId);
        Payment payment = TestUtil.createMockPayment(paymentID, PaymentStatus.APPROVED);

        GetPaymentOutput getPaymentOutput = GetPaymentOutput.from(payment);

        final var paymentResponse = PaymentResponse.from(getPaymentOutput);
                
        when(getPaymentByIdUseCase.execute(eq(expectedId))).thenReturn(getPaymentOutput);

        // When
        final var request = get("/payments/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(paymentResponse));


                System.err.println(this.mapper.writeValueAsString(paymentResponse));

        // Then
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.payment_id", equalTo(expectedId)))
                .andExpect(jsonPath("$.payment_status", equalTo(expectedStatus)))
                .andExpect(jsonPath("$.amount", equalTo(expectedAmount)));

    }

    @Test
    public void givenAnInvalidId_whenCallsGetPaymentById_shouldReturnNotFound() throws Exception {
        // Given
        final var expectedId = "nonExistingPayment";
        
        when(getPaymentByIdUseCase.execute(eq(expectedId)))
            .thenThrow(IDNotFoundUtils.notFound(PaymentID.from(expectedId), Payment.class).get());

        // When
        final var request = get("/payments/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        // Then
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
