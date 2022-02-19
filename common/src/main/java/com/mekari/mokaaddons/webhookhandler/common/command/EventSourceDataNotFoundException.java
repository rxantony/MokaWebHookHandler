package com.mekari.mokaaddons.webhookhandler.common.command;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public class EventSourceDataNotFoundException extends CommandEventException {
    public EventSourceDataNotFoundException(Event event) {
        this(String.format("eventId:%s-eventName:%s-dataId:%s is not found on event_source", 
                event.getHeader().getEventId(), event.getHeader().getEventName(), event.getBody().getData().getId()), event);
    }
     
    public EventSourceDataNotFoundException(String message, Event event) {
        super(message, event);
    }
}