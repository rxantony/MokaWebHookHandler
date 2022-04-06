package com.mekari.mokaaddons.common.webhook.moka;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.springframework.util.Assert;

import lombok.AllArgsConstructor;

public class EventNameClassMap {

    private final HashMap<String, Item> eventClassMaps = new HashMap<>();

    public <TEvent extends AbstractEvent, TRequest extends EventRequest> EventNameClassMap add(String eventName,
        Class<TEvent> eventClass) {

        return add(eventName, eventClass, null);
    }

    public <TEvent extends AbstractEvent, TRequest extends EventRequest> EventNameClassMap add(String eventName,
            Class<TEvent> eventClass, Class<TRequest> requestClass) {

        Assert.notNull(eventClass, "eventClass must not be null");
        eventClassMaps.put(eventName, new Item(eventClass, requestClass));
        return this;
    }

    @SuppressWarnings("unchecked") 
    public Class<AbstractEvent> gerEventClass(String eventName) {
        var item  = eventClassMaps.get(eventName);
        if(item == null) 
            return null;
        return (Class<AbstractEvent>) item.eventClass;
    }

    @SuppressWarnings("unchecked") 
    public Class<EventRequest> gerRequestlass(String eventName) {
        var item  = eventClassMaps.get(eventName);
        if(item == null) 
            return null;
        return (Class<EventRequest>) item.requestClass;
    }

    @AllArgsConstructor
    public static class Item {
        public final Type eventClass;
        public final Type requestClass;
    }
}