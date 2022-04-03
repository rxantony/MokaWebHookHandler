package com.mekari.mokaaddons.common.webhook;

public class CommandException extends WebhookHandlingException {
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
