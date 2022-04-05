package com.mekari.mokaaddons.common.webhook.moka;

import com.mekari.mokaaddons.common.webhook.WebhookHandlingException;

public class EventSourceNotFoundException extends WebhookHandlingException {
    public final AbstractEvent Event;
    
    public EventSourceNotFoundException(AbstractEvent event) {
        this(String.format("eventId:%s-bodyId:%s is not found on event_source", event.getHeader().getEventId(), event.getBody().getData().getId()), event);
    }
     
    public EventSourceNotFoundException(String message, AbstractEvent event) {
        super(message);
        this.Event = event;
    }
}