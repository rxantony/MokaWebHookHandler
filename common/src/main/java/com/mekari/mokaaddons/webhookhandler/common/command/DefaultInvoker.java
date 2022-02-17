package com.mekari.mokaaddons.webhookhandler.common.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.WebHookHandlingException;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DefaultInvoker implements Invoker {

    private @Autowired ObjectMapper mapper;
    private @Autowired CommandEventManager manager;
    private String eventNamePrefix;
    private Logger logger;

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation.
     * this constuctor is neccessary by springboot to instantiate this class.
     */
    public DefaultInvoker() {
        init();
    }

    public DefaultInvoker(@Autowired CommandEventManager manager, @Autowired ObjectMapper mapper) {
        this(manager, mapper, null);
    }

    public DefaultInvoker(CommandEventManager manager, ObjectMapper mapper, String eventNamePrefix) {
        Assert.notNull(manager, "managger must not be null");
        Assert.notNull(mapper, "mapper must not be null");

        this.manager = manager;
        this.mapper = mapper;
        init();
    }

    protected void init() {
        eventNamePrefix = eventNamePrefix != null ? eventNamePrefix.trim() : eventNamePrefix;
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    protected final Logger getLogger() {
        return logger;
    }

    @Override
    public final void invoke(String event) throws Exception {
        beforeInvoke(event);

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
            afterInvoke(event, eventObj, iex);
            if (iex != null)
                throw iex;
        }
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

    // hook methods
    protected void beforeInvoke(String message) {
    }

    protected void afterInvoke(String message, Event event, Exception ex) {
    }
}
