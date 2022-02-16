package com.mekari.mokaaddons.webhookhandler.common.consumer;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;

public class UnknownEventFormatException extends WebHookHandlingException {

    private final String event;

    public UnknownEventFormatException(String message, String event) {
        super(message);
        this.event = event;
    }

    public String Event() {
        return event;
    }
}
