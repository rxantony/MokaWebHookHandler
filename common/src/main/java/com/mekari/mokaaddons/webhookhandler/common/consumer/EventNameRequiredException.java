package com.mekari.mokaaddons.webhookhandler.common.consumer;

public class EventNameRequiredException extends UnknownEventFormatException {

    private final String eventId;

    public EventNameRequiredException(String eventId, String event) {
        super(String.format("eventName is required for eventId:", eventId), event);
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}
