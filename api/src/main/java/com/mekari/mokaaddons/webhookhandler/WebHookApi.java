package com.mekari.mokaaddons.webhookhandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.command.CommandEventManager;
import com.mekari.mokaaddons.webhookhandler.common.event.JsonEventValidator;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage.Item;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebHookApi {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CommandEventManager manager;

    @Autowired
    private JsonEventValidator validator;

    @Autowired
    private DeadLetterStorage deadLetterStorage;

    private static final Logger LOGGER = LogManager.getFormatterLogger(WebHookApi.class);

    // it respresents the api endpoint which will be called by partner.
    public void handle(String message) {
        LOGGER.debug("receice webhook message, with payload:%s", message);

        JsonNode eventNode = null;
        try {
            eventNode = mapper.readTree(message);
            validator.validate(eventNode);
            var eventName = eventNode.get("header").get("event_name").asText();
            var eventCmd = manager.createCommand(eventName);
            var event = mapper.readValue(eventNode.traverse(), eventCmd.eventClass());
            eventCmd.execute(event);
        } catch (Exception ex) {
            saveDeadLetter(message, eventNode, ex);
        }
    }

    private void saveDeadLetter(String message, JsonNode eventNode, Exception ex){
        try{
            var builder = Item.builder()
                            .payload(message)
                            .reason(ex.toString())
                            .source(this.getClass().getName())
                            .createdAt(DateUtil.now());

            if(eventNode != null){
                var headerNode = eventNode.get("header");
                if(headerNode != null){
                    var eventIdNode = headerNode.get("event_id");
                    if(eventIdNode != null)
                        builder.eventId(eventIdNode.asText());
                }
            }
            deadLetterStorage.insert(builder.build());
        }
        catch(Exception iex){
            LOGGER.error(iex.toString());
        }
    }
}