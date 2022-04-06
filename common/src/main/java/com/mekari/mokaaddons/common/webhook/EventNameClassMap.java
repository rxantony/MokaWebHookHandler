package com.mekari.mokaaddons.common.webhook;

import java.util.HashMap;
import java.util.function.Function;

import com.mekari.mokaaddons.common.handler.Request;

import org.springframework.util.Assert;

import lombok.AllArgsConstructor;

public class EventNameClassMap {

    private final HashMap<String, MapItem> eventClassMaps = new HashMap<>();

    public <TEvent extends Event> EventNameClassMap add(String eventName,
        Class<TEvent> eventClass) {

        return add(eventName, eventClass, null);
    }

    @SuppressWarnings("unchecked") 
    public <TEvent extends Event> EventNameClassMap add(String eventName,
            Class<TEvent > eventClass, Function<TEvent, Request<Void>> requestFactory) {

        Assert.notNull(eventClass, "eventClass must not be null");
        var item = new MapItem((Class<Event>)eventClass, (Function<Event, Request<Void>>) requestFactory);
        eventClassMaps.put(eventName, item);
        return this;
    }

    public Class<Event> gerEventClass(String eventName) {
        return get(eventName).eventClass;
    }

    public MapItem get(String eventName) {
        var item  = eventClassMaps.get(eventName);
        if(item == null) 
            return null;
        return item;
    }

    @AllArgsConstructor
    public static class MapItem {
        public final Class<Event> eventClass;
        public final Function<Event, Request<Void>> requestFactory;
    }
}