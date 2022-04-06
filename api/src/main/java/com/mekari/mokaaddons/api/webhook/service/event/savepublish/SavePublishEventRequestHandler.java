package com.mekari.mokaaddons.api.webhook.service.event.savepublish;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.api.config.AppConstant;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.infrastructure.messaging.Publisher;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.BuilderUtil;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage.NewItem;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SavePublishEventRequestHandler extends AbstractVoidRequestHandler<SavePublishEventRequest> {
    private @Autowired ObjectMapper mapper;
    private @Autowired EventNameClassMap eventClsMap;
    private @Autowired DeadLetterStorage deadLetterStorage;
    private @Autowired EventSourceStorage eventStorage;
    private @Autowired Publisher publisher;

    private static final Logger LOGGER = LogManager.getFormatterLogger(SavePublishEventRequestHandler.class);

    @Override
    protected void handleInternal(SavePublishEventRequest request) throws Exception{
        String json = null;
        JsonNode jsonNode = null;
        try{
            json = request.getJson();
            jsonNode = mapper.readTree(json);
            var eventName = jsonNode.get("header").get("event_name").asText();
            var eventCls = eventClsMap.gerEventClass(eventName);
            var event = (AbstractEvent)mapper.readValue(jsonNode.traverse(), eventCls);
            saveEvent(event);
            publishEvent(event);
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

    private void saveEvent(AbstractEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.info("insert event into even_store eventId:%s-eventName:%s-dataId:%s with updatedAt:%s",
                header.getEventId(), header.getEventName(), data.getId(), header.getTimestamp().toString());

        eventStorage
            .insert(NewItem
                    .builder()
                    .dataId(data.getId().toString())
                    .eventDate(header.getTimestamp())
                    .eventName(header.getEventName())
                    .payload(mapper.writeValueAsString(event))
                    .eventId(header.getEventId())
                    .outletId(header.getOutletId())
                    .version(header.getVersion())
                    .timestamp(header.getTimestamp())
                    .createdAt(DateUtil.now())
                    .build());
    }

    private void publishEvent(AbstractEvent event) {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.info(
                "publish webHookEventReceived eventId:%s-eventName:%s-dataId:%s with eventDate:%s into Queue:%sQueue",
                header.getEventId(), header.getEventName(), data.getId(), header.getTimestamp().toString(),
                AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE);

        publisher.publish(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE, event);
    }
}
