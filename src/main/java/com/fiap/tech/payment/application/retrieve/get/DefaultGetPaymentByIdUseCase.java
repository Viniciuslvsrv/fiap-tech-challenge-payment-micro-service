package com.fiap.tech.payment.application.retrieve.get;

import com.fiap.tech.payment.application.util.IDNotFoundUtils;
import com.fiap.tech.payment.domain.payment.Payment;
import com.fiap.tech.payment.domain.payment.PaymentGateway;
import com.fiap.tech.payment.domain.payment.PaymentID;

public class DefaultGetPaymentByIdUseCase extends GetPaymentByIdUseCase{

    private final PaymentGateway paymentGateway;    

    public DefaultGetPaymentByIdUseCase(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
    
    @Override
    public GetPaymentOutput execute(String anIN) {
        PaymentID paymentID = PaymentID.from(anIN);

        return paymentGateway.findById(paymentID)
                             .map(GetPaymentOutput::from)
                             .orElseThrow(IDNotFoundUtils.notFound(paymentID, Payment.class));
    }

}
