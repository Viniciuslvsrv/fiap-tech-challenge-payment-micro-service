package com.fiap.tech.payment.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIN);
}
