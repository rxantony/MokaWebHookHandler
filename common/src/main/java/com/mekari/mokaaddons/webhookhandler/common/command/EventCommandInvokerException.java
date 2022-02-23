package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;

public class EventCommandInvokerException extends WebHookHandlingException {
    public final String event;

    public EventCommandInvokerException(String event, Throwable cause) {
        super(cause);
        this.event = event;
    }
}