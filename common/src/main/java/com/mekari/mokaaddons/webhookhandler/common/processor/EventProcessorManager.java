package com.mekari.mokaaddons.webhookhandler.common.processor;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

public interface EventProcessorManager {
     <TEvent extends Event> EventProcessor<TEvent> createProcessor(String eventName) throws ClassNotFoundException;
}
