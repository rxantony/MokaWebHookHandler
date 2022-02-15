package com.mekari.mokaaddons.webhookhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.processor.EventProcessorManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebHookApi {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EventProcessorManager manager;

    private static final Logger LOGGER = LogManager.getFormatterLogger(WebHookApi.class);

    // it respresents the api endpoint which will be called by partner.
    public void handle(String event) {
        LOGGER.debug("receice webhook event, with payload:%s", event);
        try {
            var eventNode = mapper.readTree(event);
            var eventHeaderNode = eventNode.get("header");
            var eventName = eventHeaderNode.get("event_name").asText();
            var eventProcessor = manager.createProcessor(eventName);
            var eventObj = mapper.readValue(eventNode.traverse(), eventProcessor.eventClass());
            eventProcessor.process(eventObj);
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }
}