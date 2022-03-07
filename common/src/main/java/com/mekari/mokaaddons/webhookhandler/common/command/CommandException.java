package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public class CommandException extends WebHookHandlingException {
    public final Event event;

    public CommandException(String message, Event event) {
        this(message, event, null);
    }

    public CommandException(Exception inner, Event event) {
        this(null, event, inner);
    }

    public CommandException(String message, Event event, Throwable inner) {
        super(message, inner);
        this.event = event;
    }
}
