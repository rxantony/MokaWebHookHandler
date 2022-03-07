package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public class DataNotFoundException extends CommandException {
    public DataNotFoundException(Event event) {
        this(String.format("eventId:%s-bodyId:%s is not found on event_source", event.geId(), event.getBody().getId()), event);
    }
     
    public DataNotFoundException(String message, Event event) {
        super(message, event);
    }
}