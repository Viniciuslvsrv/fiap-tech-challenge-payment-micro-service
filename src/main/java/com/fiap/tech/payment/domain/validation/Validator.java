package com.fiap.tech.payment.domain.validation;

public abstract class Validator {

    private final ValidationHandler handler;
    
    public abstract void validate();

    public ValidationHandler getHandler() {
        return handler;
    }

    protected Validator(final ValidationHandler aHandler) {
        this.handler = aHandler;
    }
}
