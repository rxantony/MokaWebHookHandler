package com.mekari.mokaaddons.common.webhook.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.command.Command;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.webhook.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.EventRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public abstract class AbstractEventMapConsumer {

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

    protected void handle(String json) throws Exception{
        var request = getRequest(json);
        handlerManager.handle(request);
    }

    protected void handle(String json, Command<EventRequest> handler) throws Exception{
        var request = getRequest(json);
        handler.execute(request);
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
