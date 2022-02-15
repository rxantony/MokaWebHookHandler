package com.mekari.mokaaddons.webhookhandler.common.processor;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public interface EventProcessor<TEvent extends Event> {
    void process(TEvent event) throws EventProcessingException;

    Class<TEvent> eventClass();
}
