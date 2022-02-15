package com.mekari.mokaaddons.webhookhandler.common;

public class WebHookHandlingException extends Exception {

    public WebHookHandlingException(String message) {
        super(message);
    }

    public WebHookHandlingException(Exception inner) {
        super(inner);
    }

    public WebHookHandlingException(String message, Exception inner) {
        super(message, inner);
    }
}
