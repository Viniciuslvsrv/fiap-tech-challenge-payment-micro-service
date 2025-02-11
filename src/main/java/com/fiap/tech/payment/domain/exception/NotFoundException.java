package com.fiap.tech.payment.domain.exception;

import com.fiap.tech.payment.domain.validation.Error;
import com.fiap.tech.payment.domain.AggregateRoot;
import com.fiap.tech.payment.domain.Identifier;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends DomainException {

    protected NotFoundException(String aMessage, List<Error> anErrors) {
        super(aMessage, anErrors);

    }

    public static NotFoundException with(
        final Class<? extends AggregateRoot<?>> anAggregate,
        final Identifier id
    ) {
        final var anErrorMessage = "%s with ID %s was not found".formatted(anAggregate.getSimpleName(), id.getValue());
        return new NotFoundException(anErrorMessage, Collections.emptyList());
    }
}
