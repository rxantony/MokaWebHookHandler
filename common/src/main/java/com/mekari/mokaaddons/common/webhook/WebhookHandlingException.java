package com.mekari.mokaaddons.common.webhook;

public class WebhookHandlingException extends RuntimeException {

    public WebhookHandlingException(String message) {
        super(message);
    }

    public WebhookHandlingException(Throwable inner) {
        super(inner);
    }

    public WebhookHandlingException(String message, Throwable inner) {
        super(message, inner);
    }
}