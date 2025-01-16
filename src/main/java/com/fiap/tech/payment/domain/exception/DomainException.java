package com.fiap.tech.payment.domain.exception;

import com.fiap.tech.payment.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStacktraceRuntimeException {

    protected final List<Error> errors;

    protected DomainException(final String message, final List<Error> anErrors) {
        super(message);
        this.errors = anErrors;
    }
    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }
    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), List.of(anError));
    }
    public List<Error> getErrors() {
        return errors;
    }
}
