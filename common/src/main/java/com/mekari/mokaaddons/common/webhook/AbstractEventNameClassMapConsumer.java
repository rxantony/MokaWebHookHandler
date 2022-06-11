package com.mekari.mokaaddons.common.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.command.Command;
import com.mekari.mokaaddons.common.handler.Request;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;

import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public abstract class AbstractEventNameClassMapConsumer {

    private @Autowired ObjectMapper mapper;
    private @Autowired RequestHandlerManager handlerManager;
    private final EventNameClassMap eventClassMap;
    protected final Logger logger;

    protected AbstractEventNameClassMapConsumer(RequestHandlerManager handlerManager, ObjectMapper mapper, EventNameClassMap eventClassMap){
        this(eventClassMap);
        Assert.notNull(handlerManager, "handlerManager must not be null");
        Assert.notNull(mapper, "mapper must not be null");
        this.handlerManager = handlerManager;
        this.mapper = mapper;
    }

    protected AbstractEventNameClassMapConsumer(EventNameClassMap eventClassMap){
        Assert.notNull(eventClassMap, "eventClassMap must not be null");
        this.eventClassMap = eventClassMap;
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    protected RequestHandlerManager getHandlerManager(){
        return handlerManager;
    }
    
    protected void handle(String json) throws Exception{
        handle(json, null);
    }

    protected void handle(String json, Command<Request<Void>> command) throws Exception{
        var ctx = getRequestContext(json);
        handleInternal(ctx, command);
    }
    
    private void handleInternal(Triple<String, Event, Request<Void>> ctx, Command<Request<Void>> command) throws Exception{
        try {
            if(command == null){
                //straighforward execute the request through handlerManager.
                handlerManager.handle(ctx.getRight());
            }
            else {
                //let client decide how to execute the request.
                command.execute(ctx.getRight());
            }
        }
        catch(EventHandlingException ex){
            if(Strings.isBlank(ex.getPayload()))
                ex.setPayload(ctx.getLeft());
            throw ex;
        }
        catch(Exception ex){
            //inserting to deadletter willbe handled at Kafka or RabbitMq WebhookErrorHandler class.
            throw new EventHandlingException(ex, ctx.getLeft(), ctx.getMiddle(), this.getClass().getName());
        }
    }

    private Triple<String, Event, Request<Void>> getRequestContext(String json) throws EventHandlingException{
        try{
            logger.debug("consume message:%s", json);
            var jsonNode = mapper.readTree(json);
            var eventName = jsonNode.get("header").get("event_name").asText();
            var mapItem = eventClassMap.get(eventName);
            var event = mapper.readValue(jsonNode.traverse(), mapItem.eventClass);
            return Triple.of(json, event, mapItem.requestFactory.apply(event));
        }
        catch(Exception ex){
            throw new EventHandlingException(ex, json, this.getClass().getName());
        }
    }
}
