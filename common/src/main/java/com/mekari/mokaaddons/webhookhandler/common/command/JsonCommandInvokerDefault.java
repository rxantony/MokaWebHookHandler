package com.mekari.mokaaddons.webhookhandler.common.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.event.UnknownEventFormatException;
import com.mekari.mokaaddons.webhookhandler.common.event.validator.JsonEventValidatorManager;
import com.mekari.mokaaddons.webhookhandler.common.util.SingletonUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.Builder;

@Component
public class JsonCommandInvokerDefault implements CommandInvoker {

    private @Autowired ObjectMapper mapper;
    private @Autowired CommandManager commandManager;
    private @Autowired JsonEventValidatorManager validatorManager;
    private String eventNamePrefix;
    private static final Logger LOGGER = LogManager.getFormatterLogger(JsonCommandInvokerDefault.class);

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary by springboot to instantiate this class.
     */
    public JsonCommandInvokerDefault() {
    }

    public JsonCommandInvokerDefault(String eventNamePrefix) {
        this.eventNamePrefix = eventNamePrefix;
        init();
    }

    public JsonCommandInvokerDefault(Config config) {
        Assert.notNull(config, "config must not be null");

        this.commandManager = config.commandManager;
        this.validatorManager = config.validatorManager;
        this.mapper = config.mapper;
        this.eventNamePrefix = config.eventNamePrefix;
        init();
    }

    private void init() {
        this.eventNamePrefix = eventNamePrefix != null ? eventNamePrefix.trim() : eventNamePrefix;
    }

    @Override
    public final void invoke(String jsonEvent) throws JsonCommandInvokerException {
        Event eventObj = null;
        JsonNode eventNode = null;
        try {
            eventNode = mapper.readTree(jsonEvent);
            var eventName = validateJsonAndGetEventName(eventNode);
            var eventCmd = commandManager.createCommand(eventName);
            eventObj = mapper.readValue(eventNode.traverse(), eventCmd.eventClass());
            eventCmd.execute(eventObj);
        } catch (Exception ex) {
            throw new JsonCommandInvokerException (jsonEvent, eventNode, ex);
        }
    }
     

    private String validateJsonAndGetEventName(JsonNode eventNode) throws UnknownEventFormatException{
        var defaultValidator = validatorManager.getDeafultValidator();
        if(defaultValidator == null)
            defaultValidator = SingletonUtil.DEFAULT_JSONEVENT_VALIDATOR;
        defaultValidator.validate(eventNode);

        var eventName = getEventName(eventNode);
        var validator = validatorManager.crateValidator(eventName);
        if(validator != null)
            validator.validate(eventNode);
        return eventName;
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
        private JsonEventValidatorManager validatorManager;
        private ObjectMapper mapper;
        private String eventNamePrefix;

        public Config(CommandManager commandManager
                , JsonEventValidatorManager validatorManager
                , ObjectMapper mapper
                , String eventNamePrefix) {
            Assert.notNull(commandManager , "commandManager must not be null");
            Assert.notNull(validatorManager, "validatorManager must not be null");
            Assert.notNull(mapper, "mapper must not be null");

            this.commandManager = commandManager;
            this.validatorManager = validatorManager;
            this.mapper = mapper;
            this.eventNamePrefix = eventNamePrefix;
        }
    }
}
