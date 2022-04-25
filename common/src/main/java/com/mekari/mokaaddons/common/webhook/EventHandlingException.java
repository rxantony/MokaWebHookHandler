package com.mekari.mokaaddons.common.webhook;
import org.springframework.util.Assert;

public class EventHandlingException extends WebhookHandlingException {
    private final String source;
    private final Event event;
     
    public EventHandlingException(String message, Event event) {
        this(message, event, null);
    }

    public EventHandlingException(String message, Event event, String source) {
        super(message);
        Assert.notNull(event, "event must not be null");
        this.event = event;
        this.source = source;
    }

    public EventHandlingException(Throwable cause, Event event) {
        this(cause, event, null);
    }

    public EventHandlingException(Throwable cause, Event event, String source) {
        super(cause);
        Assert.notNull(event, "event must not be null");
        this.event = event;
        this.source = source;
    }

    public String getSource(){
        return source;
    }

    public Event getEvent(){
        return event;
    }
}