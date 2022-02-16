package com.mekari.mokaaddons.webhookhandler.common.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandManager;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.util.Assert;

public abstract class AbstractConsumer {

    private ObjectMapper mapper;
    private CommandManager commandManager;

    protected final String eventNamePrefix;
    protected final Logger logger;

    private static final String X_REJECTED_INFO = "reason";

    protected AbstractConsumer(CommandManager commandManager, ObjectMapper mapper) {
        this(commandManager, mapper, null);
    }

    protected AbstractConsumer(CommandManager commandManager, ObjectMapper mapper, String eventNamePrefix) {
        Assert.notNull(commandManager, "commandManager must not be null");
        Assert.notNull(mapper, "mapper must not be null");

        this.commandManager = commandManager;
        this.mapper = mapper;
        this.eventNamePrefix = eventNamePrefix!=null ? eventNamePrefix.trim() : eventNamePrefix;
        logger = LogManager.getLogger(this.getClass());
    }

    public void consume(Message message, Channel channel) throws Exception {
        try{
            var event = new String(message.getBody());
            var eventNode = mapper.readTree(event);
            var eventName = validateAndGetEventName(event, eventNode);
            var eventCmd = commandManager.createCommand(eventName);
            var eventObj = mapper.readValue(eventNode.traverse(), eventCmd.eventClass());
            eventCmd.execute(eventObj);
        }
        catch(Exception ex){
            //TODO need a thorough reserching for send actual error message cause the message is rejected
            logger.error(ex.getMessage());
            message.getMessageProperties().setAppId("cxdxrx");
            var header = message.getMessageProperties().getHeaders();
            if(header.get(X_REJECTED_INFO) == null) 
                header.put(X_REJECTED_INFO, ex.getMessage());
            throw ex;
        }
    }

    protected String validateAndGetEventName(String event, JsonNode eventNode) throws WebHookHandlingException {
        var header = eventNode.get("header");
        var eventIdNode = header.get("event_id");
        if (eventIdNode == null)
            throw new UnknownEventFormatException("eventId is required", event);

        var eventNameNode = header.get("event_names");
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
