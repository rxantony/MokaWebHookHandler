package com.mekari.mokaaddons.webhookhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebHookApi {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CommandManager manager;

    private static final Logger LOGGER = LogManager.getFormatterLogger(WebHookApi.class);

    // it respresents the api endpoint which will be called by partner.
    public void handle(String message) {
        LOGGER.debug("receice webhook message, with payload:%s", message);
        try {
            var eventNode = mapper.readTree(message);
            var eventHeaderNode = eventNode.get("header");
            var eventName = eventHeaderNode.get("event_name").asText();
            var eventCmd = manager.createCommand(eventName);
            var event = mapper.readValue(eventNode.traverse(), eventCmd.eventClass());
            eventCmd.execute(event);
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }
}