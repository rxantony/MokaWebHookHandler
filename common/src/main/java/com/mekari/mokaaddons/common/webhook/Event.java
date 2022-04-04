package com.mekari.mokaaddons.common.webhook;

public interface Event {
    
    EventHeader getHeader();

    Object getBody();
}