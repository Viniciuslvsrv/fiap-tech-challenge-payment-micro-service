package com.fiap.tech.payment.infra.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fiap.tech.payment.infra.payment.model.PaymentResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;

@RequestMapping(value = "payments")
@Tag(name = "Payments")
public interface PaymentAPI {

    @GetMapping(
        value = "{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a payment by its identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Payment was not found"),
        @ApiResponse(responseCode = "500", description = "An internal error was thrown")
    })
    PaymentResponse getById(@PathVariable(name = "id") String paymentID);
    
}
