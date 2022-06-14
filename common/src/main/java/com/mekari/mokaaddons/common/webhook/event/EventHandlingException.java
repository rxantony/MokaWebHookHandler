package com.mekari.mokaaddons.common.webhook.event;
import org.springframework.util.Assert;

import com.mekari.mokaaddons.common.webhook.WebhookHandlingException;

public class EventHandlingException extends WebhookHandlingException {
    private String payload;
    private final Event event;
    private final String source;

    public EventHandlingException(String errMessage, Event event) {
        this(errMessage, null, event, null);
    }

    public EventHandlingException(String errMessage, String payload) {
        this(errMessage, payload, null, null);
    }

    public EventHandlingException(String errMessage, Event event, String source) {
        this(errMessage, null, event, source);
    }

    public EventHandlingException(String errMessage, String payload, String source) {
        this(errMessage, payload, null, source);
    }

    public EventHandlingException(String errMessage, String payload, Event event, String source) {
        super(errMessage);
        Assert.notNull(event, "event must not be null");
        this.payload = payload;
        this.event = event;
        this.source = source;
    }

    public EventHandlingException(Throwable cause, Event event) {
        this(cause, null, event, null);
    }

    public EventHandlingException(Throwable cause, String payload) {
        this(cause, payload, null, null);
    }

    public EventHandlingException(Throwable cause, Event event, String source) {
        this(cause, null, event, source);
    }

    public EventHandlingException(Throwable cause, String payload, String source) {
        this(cause, payload, null, source);
    }

    public EventHandlingException(Throwable cause, String payload, Event event, String source) {
        super(cause);
        Assert.notNull(event, "event must not be null");
        this.payload = payload;
        this.event = event;
        this.source = source;
    }

    public String getSource(){
        return source;
    }

    public String getPayload(){
        return payload;
    }

    void setPayload(String payload){
        this.payload = payload;
    }

    public Event getEvent(){
        return event;
    }
}