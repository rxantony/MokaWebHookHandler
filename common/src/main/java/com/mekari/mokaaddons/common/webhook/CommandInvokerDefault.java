package com.mekari.mokaaddons.common.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.Builder;

@Component
public class CommandInvokerDefault implements CommandInvoker {

    private @Autowired ObjectMapper mapper;
    private @Autowired CommandManager commandManager;
    private String eventNamePrefix;
    private static final Logger LOGGER = LogManager.getFormatterLogger(CommandInvokerDefault.class);

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary by springboot to instantiate this class.
     */
    public CommandInvokerDefault() {
    }

    public CommandInvokerDefault(String eventNamePrefix) {
        this.eventNamePrefix = eventNamePrefix;
        init();
    }

    public CommandInvokerDefault(Config config) {
        Assert.notNull(config, "config must not be null");

        this.commandManager = config.commandManager;
        this.mapper = config.mapper;
        this.eventNamePrefix = config.eventNamePrefix;
        init();
    }

    private void init() {
        this.eventNamePrefix = eventNamePrefix != null ? eventNamePrefix.trim() : eventNamePrefix;
    }

    @Override
    public final void invoke(String jsonEvent) throws CommandInvokerException {
        Event eventObj = null;
        JsonNode eventNode = null;
        try {
            eventNode = mapper.readTree(jsonEvent);
            validate(eventNode);
            var eventName = getEventName(eventNode);
            var eventCmd = commandManager.createCommand(eventName);
            eventObj = mapper.readValue(eventNode.traverse(), eventCmd.eventClass());
            eventCmd.execute(eventObj);
        } catch (Exception ex) {
            throw new CommandInvokerException (jsonEvent, eventNode, ex);
        }
    }
     
    private void validate(JsonNode eventNode) throws UnknownEventFormatException {
        var headerNode = eventNode.get("header");
        if (headerNode == null)
            throw new UnknownEventFormatException("header is required", eventNode.toString());

        var eventIdNode = headerNode.get("event_id");
        if (eventIdNode == null)
            throw new UnknownEventFormatException("eventId is required", eventNode.toString());

        if (headerNode.get("event_name") == null)
            throw new UnknownEventFormatException(
                String.format("event_name is required for eventId:%s", eventIdNode.asText()), eventNode.toString());

        if(headerNode.get("timestamp") == null)
            throw new UnknownEventFormatException(
                String.format("header.timestamp is required for eventId:%s", eventIdNode.asText()), eventNode.toString());
    }
    
    private String getEventName(JsonNode eventNode) {
        var eventName = eventNode.get("header").get("event_name").asText();
        if (Strings.isNotBlank(eventNamePrefix))
            return eventNamePrefix + ":" + eventName;
        return eventName;
    }

    @Builder
    public static class Config {
        private CommandManager commandManager;
        private ObjectMapper mapper;
        private String eventNamePrefix;

        public Config(CommandManager commandManager
                , ObjectMapper mapper
                , String eventNamePrefix) {
            Assert.notNull(commandManager , "commandManager must not be null");
            Assert.notNull(mapper, "mapper must not be null");

            this.commandManager = commandManager;
            this.mapper = mapper;
            this.eventNamePrefix = eventNamePrefix;
        }
    }
}
