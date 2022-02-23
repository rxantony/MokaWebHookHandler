package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public class EventCommandException extends WebHookHandlingException {
    public final Event event;

    public EventCommandException(String message, Event event) {
        this(message, null, event);
    }

    public EventCommandException(Exception inner, Event event) {
        this(null, inner, event);
    }

    public EventCommandException(String message, Exception inner, Event event) {
        super(message, inner);
        this.event = event;
    }
}
