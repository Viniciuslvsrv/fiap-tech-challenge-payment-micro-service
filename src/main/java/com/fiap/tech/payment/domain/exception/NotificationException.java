package com.fiap.tech.payment.domain.exception;

import com.fiap.tech.payment.domain.validation.handler.Notification;
import com.fiap.tech.payment.domain.validation.handler.ThrowsValidationHandler;

public class NotificationException extends DomainException {

    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
    public NotificationException(final String message, final ThrowsValidationHandler notification) {
        super(message, notification.getErrors());
    }
}
