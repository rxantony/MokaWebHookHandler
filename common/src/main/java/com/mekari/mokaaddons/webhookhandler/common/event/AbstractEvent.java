package com.mekari.mokaaddons.webhookhandler.common.event;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractEvent implements Event {
    
    @Override
    @JsonIgnore()
    public String geId() {
        return getHeader().getEventId();
    }

    @Override
    @JsonIgnore()
    public String getName(){
        return this.getHeader().getEventName();
    }

    @Override
    @JsonIgnore()
    public OffsetDateTime getDate() {
        return getHeader().getTimestamp();
    }

    public abstract EventHeader getHeader();
    public abstract AbstractEventBody<?> getBody();
}
