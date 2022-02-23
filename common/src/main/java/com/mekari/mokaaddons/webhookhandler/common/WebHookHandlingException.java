package com.mekari.mokaaddons.webhookhandler.common;

public class WebHookHandlingException extends Exception {

    public WebHookHandlingException(String message) {
        super(message);
    }

    public WebHookHandlingException(Throwable inner) {
        super(inner);
    }

    public WebHookHandlingException(String message, Throwable inner) {
        super(message, inner);
    }
}
