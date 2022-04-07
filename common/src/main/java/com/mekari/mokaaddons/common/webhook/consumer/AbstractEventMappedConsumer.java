package com.mekari.mokaaddons.common.webhook.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.webhook.EventNameClassMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public abstract class AbstractEventMappedConsumer {
    private @Autowired ObjectMapper mapper;
    private @Autowired RequestHandlerManager requestManager;
    private final EventNameClassMap eventClassMap;
    protected final Logger logger;

    protected AbstractEventMappedConsumer(EventNameClassMap eventClassMap){
        Assert.notNull(eventClassMap, "eventClassMap must not be null");
        this.eventClassMap = eventClassMap;
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    protected void consume(String json) throws Exception{
        logger.debug("consume message:%s", json);
        var jsonNode = mapper.readTree(json);
        var eventName = jsonNode.get("header").get("event_name").asText();
        var mapItem = eventClassMap.get(eventName);
        var event = mapper.readValue(jsonNode.traverse(), mapItem.eventClass);
        var request = mapItem.requestFactory.apply(event);
        requestManager.handle(request);
    }
}
