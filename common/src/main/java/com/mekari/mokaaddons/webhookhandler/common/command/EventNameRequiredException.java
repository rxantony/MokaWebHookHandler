package com.mekari.mokaaddons.webhookhandler.common.command;

public class EventNameRequiredException extends UnknownEventFormatException {

    private final String eventId;

    public EventNameRequiredException(String eventId, String event) {
        super(String.format("eventName is required for eventId:$s", eventId), event);
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}
