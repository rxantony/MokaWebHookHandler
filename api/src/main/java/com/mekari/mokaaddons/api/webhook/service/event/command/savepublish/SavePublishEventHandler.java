package com.mekari.mokaaddons.api.webhook.service.event.command.savepublish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.api.config.AppConstant;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.messaging.Publisher;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.BuilderUtil;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.mekari.mokaaddons.common.webhook.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage.NewItem;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;

@Service
public class SavePublishEventHandler extends AbstractVoidRequestHandler<SavePublishEventRequest> {
    private @Autowired ObjectMapper mapper;
    private @Autowired DeadLetterStorage deadLetterStorage;
    private @Autowired EventSourceStorage eventStorage;
    private @Autowired Publisher publisher;
    private @Autowired @Qualifier("save.publish.event") EventNameClassMap eventClsMap;

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
            saveEvent(event, request.getJson());
            publishEvent(event);
        }
        catch(Exception ex){
            try{
                var builder = BuilderUtil.createDeadLetterStorageItemBuilder(jsonNode)
                    .payload(request.getJson())
                    .source(request.getSourceName())
                    .reason(ex.toString())
                    .createdAt(DateUtil.utcNow());
                deadLetterStorage.insert(builder.build());
            }
            // basically we can forward this exception into ApiExceptionHandler.
            catch(Exception iex){
                logger.error(iex.toString());
            }           
        }
    }

    private void saveEvent(AbstractEvent event, String json) throws Exception {
        var header = event.getHeader();
        var body = event.getBody();

        logger.info("insert event into even_store eventId:%s-eventName:%s-dataId:%s with updatedAt:%s",
                header.getEventId(), header.getEventName(), body.getId(), header.getTimestamp().toString());

        eventStorage
            .insert(NewItem
                    .builder()
                    .dataId(body.getId().toString())
                    .eventDate(header.getTimestamp())
                    .eventName(header.getEventName())
                    .payload(json)
                    .eventId(header.getEventId())
                    .outletId(header.getOutletId())
                    .version(header.getVersion())
                    .timestamp(header.getTimestamp())
                    .createdAt(DateUtil.utcNow())
                    .build());
    }

    private void publishEvent(AbstractEvent event) {
        var header = event.getHeader();
        var body = event.getBody();

        logger.info(
                "publish webHookEventReceived eventId:%s-eventName:%s-dataId:%s with eventDate:%s into Queue:%sQueue",
                header.getEventId(), header.getEventName(), body.getId(), header.getTimestamp().toString(),
                AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE);

        publisher.publish(AppConstant.ExchangeName.MOKA_EVENT_RECEIVED_EXCHANGE, event);
    }
}
