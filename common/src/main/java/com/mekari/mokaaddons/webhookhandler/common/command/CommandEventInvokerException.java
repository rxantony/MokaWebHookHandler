package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;

public class CommandEventInvokerException extends WebHookHandlingException {
    public final String event;

    public CommandEventInvokerException(String event, Exception cause) {
        super(cause);
        this.event = event;
    }
}