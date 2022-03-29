package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.AbstractMokaEvent;

public class EventSourceNotFoundException extends CommandException {
    public EventSourceNotFoundException(AbstractMokaEvent event) {
        this(String.format("eventId:%s-bodyId:%s is not found on event_source", event.getHeader().getEventId(), event.getBody().getData().getId()), event);
    }
     
    public EventSourceNotFoundException(String message, Event event) {
        super(message, event);
    }
}