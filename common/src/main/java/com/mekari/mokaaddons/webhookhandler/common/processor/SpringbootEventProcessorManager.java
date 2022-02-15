package com.mekari.mokaaddons.webhookhandler.common.processor;

import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringbootEventProcessorManager implements EventProcessorManager {
    @Autowired
    private ApplicationContext appContext;

    @Override
    @SuppressWarnings("unchecked")
    public <TEvent extends Event> EventProcessor<TEvent> createProcessor(String eventName)
            throws ClassNotFoundException {
        return (EventProcessor<TEvent>) appContext.getBean(eventName, EventProcessor.class);
    }
}
