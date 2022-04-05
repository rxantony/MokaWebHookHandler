package com.mekari.mokaaddons.api.service.webhook.handleEvent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.BuilderUtil;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;
import com.mekari.mokaaddons.common.webhook.moka.handler.saveAndPublishEvent.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.moka.handler.saveAndPublishEvent.SaveAndPublishEventRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandleEventRequestHandler extends AbstractVoidRequestHandler<HandleEventRequest> {
    private @Autowired ObjectMapper mapper;
    private @Autowired EventNameClassMap eventClsMap;
    private @Autowired DeadLetterStorage deadLetterStorage;
    private @Autowired RequestHandlerManager handlerManager;
    private static final Logger LOGGER = LogManager.getFormatterLogger(HandleEventRequestHandler.class);

    @Override
    protected void handleInternal(HandleEventRequest request) throws Exception{
        String json = null;
        JsonNode jsonNode = null;
        try{
            json = request.getJson();
            jsonNode = mapper.readTree(json);
            var eventName = jsonNode.get("header").get("event_name").asText();
            var eventCls = eventClsMap.gerEventClass(eventName);
            var event = (AbstractEvent)mapper.readValue(jsonNode.traverse(), eventCls);
            var savePublishRequest = new SaveAndPublishEventRequest(event);
            handlerManager.handle(savePublishRequest);
        }
        catch(Exception ex){
            try{
                var builder = BuilderUtil.createDeadLetterStorageItemBuilder(jsonNode)
                    .payload(request.getJson())
                    .source(request.getSourceName())
                    .reason(ex.toString())
                    .createdAt(DateUtil.now());
                deadLetterStorage.insert(builder.build());
            }
            // basically we can forward this exception into ApiExceptionHandler.
            catch(Exception iex){
                LOGGER.error(iex.toString());
            }           
        }
    }
}
