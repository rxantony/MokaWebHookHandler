package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public class CommandEventException extends WebHookHandlingException {
    public final Event event;

    public CommandEventException(String message, Event event) {
        this(message, null, event);
    }

    public CommandEventException(Exception inner, Event event) {
        this(null, inner, event);
    }

    public CommandEventException(String message, Exception inner, Event event) {
        super(message, inner);
        this.event = event;
    }
}
