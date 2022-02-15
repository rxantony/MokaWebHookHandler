package com.mekari.mokaaddons.webhookhandler.common.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;
import com.mekari.mokaaddons.webhookhandler.common.processor.EventProcessorManager;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.util.Assert;

public abstract class AbstractConsumer {

    private ObjectMapper mapper;
    private EventProcessorManager eventProcessorManager;

    protected final String eventNamePrefix;
    protected final Logger logger;

    protected AbstractConsumer(EventProcessorManager eventProcessorManager, ObjectMapper mapper) {
        this(eventProcessorManager, mapper, null);
    }

    protected AbstractConsumer(EventProcessorManager eventProcessorManager, ObjectMapper mapper, String eventNamePrefix) {
        Assert.notNull(eventProcessorManager, "eventProcessorManager must not be null");
        Assert.notNull(mapper, "mapper must not be null");

        this.eventProcessorManager = eventProcessorManager;
        this.mapper = mapper;
        this.eventNamePrefix = eventNamePrefix!=null ? eventNamePrefix.trim() : eventNamePrefix;
        logger = LogManager.getLogger(this.getClass());
    }

    public void consume(Message message, Channel channel) throws Exception {
        try{
            var event = new String(message.getBody());
            var eventNode = mapper.readTree(event);
            var eventName = validateAndGetEventName(event, eventNode);
            var eventProcesor = eventProcessorManager.createProcessor(eventName);
            var eventObj = mapper.readValue(eventNode.traverse(), eventProcesor.eventClass());
            eventProcesor.process(eventObj);
        }
        catch(Exception ex){
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    protected String validateAndGetEventName(String event, JsonNode eventNode) throws WebHookHandlingException {
        var header = eventNode.get("header");
        var eventIdNode = header.get("event_id");
        if (eventIdNode == null)
            throw new UnknownEventFormatException("eventId is required", event);

        var eventNameNode = header.get("event_name");
        if (eventNameNode == null)
            throw new EventNameRequiredException(eventIdNode.asText(), event);

        if (!hasEventNamePrefix())
            return eventNameNode.asText();

        return eventNamePrefix + ":" + eventNameNode.asText();
    }

    protected boolean hasEventNamePrefix(){
        return eventNamePrefix != null && eventNamePrefix.length() != 0;
    }
}
