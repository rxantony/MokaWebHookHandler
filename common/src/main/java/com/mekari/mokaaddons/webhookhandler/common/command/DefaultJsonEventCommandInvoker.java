package com.mekari.mokaaddons.webhookhandler.common.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.event.Event;
import com.mekari.mokaaddons.webhookhandler.common.util.SingletonUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import lombok.Builder;

@Component
public class DefaultJsonEventCommandInvoker implements EventCommandInvoker {

    private @Autowired ObjectMapper mapper;
    private @Autowired EventCommandManager commandManager;
    private @Autowired JsonEventValidatorManager validatorManager;
    private String eventNamePrefix;
    private Logger logger;

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary by springboot to instantiate this class.
     */
    public DefaultJsonEventCommandInvoker() {
        init();
    }

    public DefaultJsonEventCommandInvoker(Config config) {
        Assert.notNull(config, "config must not be null");

        this.commandManager = config.commandManager;
        this.validatorManager = config.validatorManager;
        this.mapper = config.mapper;
        this.eventNamePrefix = config.eventNamePrefix != null ? config.eventNamePrefix.trim() : config.eventNamePrefix;;
        init();
    }

    protected void init() {
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    protected final Logger getLogger() {
        return logger;
    }

    @Override
    public final void invoke(String event) throws JsonEventCommandInvokerException {
        Event eventObj = null;
        JsonNode eventNode = null;
        try {
            eventNode = mapper.readTree(event);
            var eventName = validateAndGetEventName(eventNode);
            var eventCmd = commandManager.createCommand(eventName);
            eventObj = mapper.readValue(eventNode.traverse(), eventCmd.eventClass());
            eventCmd.execute(eventObj);
        } catch (Exception ex) {
            throw new JsonEventCommandInvokerException (event, eventNode, eventObj, ex);
        }
    }
    
    private String validateAndGetEventName(JsonNode eventNode) throws UnknownEventFormatException{
        var defaultValidator = validatorManager.getDeafultValidator();
        if(defaultValidator == null)
            defaultValidator = SingletonUtil.DEFAULT_JSONEVENT_VALIDATOR;
        defaultValidator.validate(eventNode);

        var eventName = getEventName(eventNode, eventNamePrefix);
        var validator = validatorManager.crateValidator(eventName);
        if(validator != null)
            validator.validate(eventNode);
        return eventName;
    }
    
    protected String getEventName(JsonNode eventNode, String namePrefix) {
        var eventName = eventNode.get("header").get("event_name").asText();
        if (Strings.isNotBlank(namePrefix))
            return namePrefix + ":" + eventName;
        return eventName;
    }

    @Builder
    public static class Config {
        private EventCommandManager commandManager;
        private JsonEventValidatorManager validatorManager;
        private ObjectMapper mapper;
        private String eventNamePrefix;

        public Config(EventCommandManager commandManager
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
