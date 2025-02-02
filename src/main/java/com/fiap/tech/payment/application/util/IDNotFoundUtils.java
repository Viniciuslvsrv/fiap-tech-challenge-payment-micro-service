package com.fiap.tech.payment.application.util;

import com.fiap.tech.payment.domain.exception.DomainException;
import com.fiap.tech.payment.domain.exception.NotFoundException;
import com.fiap.tech.payment.domain.AggregateRoot;
import com.fiap.tech.payment.domain.Identifier;

import java.util.function.Supplier;

public class IDNotFoundUtils {
    
    public static Supplier<DomainException> notFound(final Identifier anId, Class<? extends AggregateRoot<?>> anAggregate) {
        return () -> NotFoundException.with(anAggregate, anId);
    }
}
