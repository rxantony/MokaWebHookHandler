package com.mekari.mokaaddons.webhookhandler.common.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.event.DefaultJsonEventValidator;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.event.JsonEventValidator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DefaultCommandEventInvoker implements CommandInvoker {

    private @Autowired ObjectMapper mapper;
    private @Autowired CommandEventManager manager;
    private @Autowired JsonEventValidator validator;
    private String eventNamePrefix;
    private Logger logger;

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary by springboot to instantiate this class.
     */
    public DefaultCommandEventInvoker() {
        init();
    }

    public DefaultCommandEventInvoker(CommandEventManager manager, ObjectMapper mapper) {
        this(manager, mapper, DefaultJsonEventValidator.SINGLETON);
    }

    public DefaultCommandEventInvoker(CommandEventManager manager, ObjectMapper mapper, JsonEventValidator validator) {
        this(manager, mapper, validator, null);
    }

    public DefaultCommandEventInvoker(CommandEventManager manager, ObjectMapper mapper, JsonEventValidator validator, String eventNamePrefix) {
        Assert.notNull(manager, "managger must not be null");
        Assert.notNull(mapper, "mapper must not be null");
        Assert.notNull(validator, "validator must not be null");

        this.manager = manager;
        this.mapper = mapper;
        this.validator = validator;
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

        Exception iex = null;
        Event eventObj = null;
        JsonNode eventNode = null;
        try {
            eventNode = mapper.readTree(event);
            validator.validate(eventNode);
    
            var eventName = getEventName(eventNode);
            var eventCmd = manager.createCommand(eventName);

            eventObj = mapper.readValue(eventNode.traverse(), eventCmd.eventClass());
            eventCmd.execute(eventObj);
        } catch (Exception ex) {
            iex = ex;
        } finally {
            afterInvoked(event, eventObj, iex);
        }
    }

    protected String getEventName(JsonNode eventNode) {
        var eventName = eventNode.get("header").get("event_name").asText();
        if (Strings.isNotBlank(eventNamePrefix))
            return eventNamePrefix + ":" + eventName;
        return eventName;
    }

    // hook methods
    protected void afterInvoked(String message, Event event, Exception ex) throws Exception {
        if(ex != null)
            throw ex;
    }
}
