package com.mekari.mokaaddons.common.webhook.moka;

import com.mekari.mokaaddons.common.webhook.WebhookHandlingException;

public class MokaEventSourceNotFoundException extends WebhookHandlingException {
    public final AbstractMokaEvent Event;
    
    public MokaEventSourceNotFoundException(AbstractMokaEvent event) {
        this(String.format("eventId:%s-bodyId:%s is not found on event_source", event.getHeader().getEventId(), event.getBody().getData().getId()), event);
    }
     
    public MokaEventSourceNotFoundException(String message, AbstractMokaEvent event) {
        super(message);
        this.Event = event;
    }
}