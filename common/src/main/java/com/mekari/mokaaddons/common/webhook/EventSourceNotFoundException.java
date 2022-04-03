package com.mekari.mokaaddons.common.webhook;

import com.mekari.mokaaddons.common.webhook.moka.AbstractMokaEvent;

public class EventSourceNotFoundException extends CommandException {
    public EventSourceNotFoundException(AbstractMokaEvent event) {
        this(String.format("eventId:%s-bodyId:%s is not found on event_source", event.getHeader().getEventId(), event.getBody().getData().getId()), event);
    }
     
    public EventSourceNotFoundException(String message, Event event) {
        super(message, event);
    }
}