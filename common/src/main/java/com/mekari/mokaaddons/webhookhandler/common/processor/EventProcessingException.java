package com.mekari.mokaaddons.webhookhandler.common.processor;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;

public class EventProcessingException extends WebHookHandlingException {
    public EventProcessingException(String message) {
        super(message);
    }

    public EventProcessingException(Exception inner) {
        super("there is an error whhile processing an event", inner);
    }
}