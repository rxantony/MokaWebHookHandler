package com.mekari.mokaaddons.common.webhook;

import com.mekari.mokaaddons.common.ApplicationException;

public class WebhookHandlingException extends ApplicationException {

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