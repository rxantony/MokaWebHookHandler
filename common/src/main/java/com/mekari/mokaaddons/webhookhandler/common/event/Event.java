package com.mekari.mokaaddons.webhookhandler.common.event;

public interface Event {
    EventHeader getHeader();
    Object getBody();
}