package com.mekari.mokaaddons.common.webhook;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.springframework.util.Assert;

public class EventNameClassMap {
    private final HashMap<String, Type> eventClassMaps = new HashMap<>();

    public <TEvent extends Event> EventNameClassMap add(String eventName, Class<TEvent> eventClass){
        Assert.notNull(eventClass, "eventClass must not be null");
        eventClassMaps.put(eventName, eventClass);
        return this;
    }

    @SuppressWarnings("unchecked")
    public Class<Event> gerEventClass(String eventName){
        return (Class<Event>)eventClassMaps.get(eventName);
    }
}