package com.mekari.mokaaddons.common.webhook;

public class UnknownEventFormatException extends WebhookHandlingException {

    private final String event;

    public UnknownEventFormatException(String message, String event) {
        super(message);
        this.event = event;
    }

    public String Event() {
        return event;
    }
}
