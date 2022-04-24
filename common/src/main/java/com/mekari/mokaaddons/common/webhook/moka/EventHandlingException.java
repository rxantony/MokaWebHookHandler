package com.mekari.mokaaddons.common.webhook.moka;

import com.mekari.mokaaddons.common.webhook.WebhookHandlingException;

import org.springframework.util.Assert;

public class EventHandlingException extends WebhookHandlingException {
    public final AbstractEvent Event;
    
    public EventHandlingException(String message, AbstractEvent event) {
        super(message);
        Assert.notNull(event, "event must not be null");
        this.Event = event;
    }

    public static EventHandlingException eventNotFoundInEventSource(AbstractEvent event){
        var msg = String.format("eventId:%s-bodyId:%s is not found on event_source", event.getHeader().getEventId(), event.getBody().getData().getId());
        return new EventHandlingException(msg, event);
    }
}