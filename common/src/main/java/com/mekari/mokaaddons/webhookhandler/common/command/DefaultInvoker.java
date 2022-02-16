package com.mekari.mokaaddons.webhookhandler.common.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class DefaultInvoker implements Invoker {

    private final ObjectMapper mapper;
    private final CommandEventManager manager;
    protected final String eventNamePrefix;
    private final Logger logger;

    public DefaultInvoker(@Autowired CommandEventManager manager, @Autowired ObjectMapper mapper) {
        this(manager, mapper, null);
    }

    public DefaultInvoker(CommandEventManager manager, ObjectMapper mapper, String eventNamePrefix) {
        Assert.notNull(manager, "managger must not be null");
        Assert.notNull(mapper, "mapper must not be null");

        this.manager = manager;
        this.mapper = mapper;
        this.eventNamePrefix = eventNamePrefix != null ? eventNamePrefix.trim() : eventNamePrefix;
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    @Override
    public final void invoke(Message message) throws Exception {

        beforeInvoke(message);

        var event = new String(message.getBody());
        var eventNode = mapper.readTree(event);

        validate(event, eventNode);

        var eventName = getEventName(eventNode);
        var eventCmd = manager.createCommand(eventName);
        var eventObj = mapper.readValue(eventNode.traverse(), eventCmd.eventClass());

        Exception iex = null;
        try {
            eventCmd.execute(eventObj);
        } catch (Exception ex) {
            iex = ex;
        } finally {
            afterInvoke(message, eventObj, iex);
            if (iex != null)
                throw iex;
        }
    }

    protected void beforeInvoke(Message message) {
    }

    protected void afterInvoke(Message message, Event event, Exception ex) {
    }

    protected String getEventName(JsonNode eventNode) {
        var eventName = eventNode.get("header").get("event_name").asText();
        if (Strings.isNotBlank(eventNamePrefix))
            return eventNamePrefix + ":" + eventName;
        return eventName;
    }

    protected void validate(String event, JsonNode eventNode) throws WebHookHandlingException {
        var header = eventNode.get("header");
        var eventIdNode = header.get("event_id");
        if (eventIdNode == null)
            throw new UnknownEventFormatException("eventId is required", event);
        if (header.get("event_name") == null)
            throw new EventNameRequiredException(eventIdNode.asText(), event);
    }

}
