package com.mekari.mokaaddons.common.webhook.event;

public interface Event {
    
    EventHeader getHeader();

    Object getBody();
}