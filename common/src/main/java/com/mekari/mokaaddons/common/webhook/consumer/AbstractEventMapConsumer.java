package com.mekari.mokaaddons.common.webhook.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.webhook.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.EventRequest;

public abstract class AbstractEventMapConsumer {
    public static interface EventHandler{
        void handle(EventRequest request) throws Exception;
    }

    private @Autowired ObjectMapper mapper;
    private @Autowired RequestHandlerManager handlerManager;
    private final EventNameClassMap eventClassMap;
    protected final Logger logger;

    protected AbstractEventMapConsumer(EventNameClassMap eventClassMap){
        Assert.notNull(eventClassMap, "eventClassMap must not be null");
        this.eventClassMap = eventClassMap;
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    protected RequestHandlerManager getHandlerManager(){
        return handlerManager;
    }

    protected void consume(String json) throws Exception{
        var request = getRequest(json);
        handlerManager.handle(request);
    }

    protected void consume(String json, EventHandler handler) throws Exception{
        var request = getRequest(json);
        handler.handle(request);
    }

    protected EventRequest getRequest(String json) throws Exception{
        logger.debug("consume message:%s", json);
        var jsonNode = mapper.readTree(json);
        var eventName = jsonNode.get("header").get("event_name").asText();
        var mapItem = eventClassMap.get(eventName);
        var event = (AbstractEvent) mapper.readValue(jsonNode.traverse(), mapItem.eventClass);
        return (EventRequest) mapItem.requestFactory.apply(event);
    }
}
