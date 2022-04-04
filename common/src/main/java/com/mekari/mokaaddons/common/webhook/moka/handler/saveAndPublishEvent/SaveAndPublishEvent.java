package com.mekari.mokaaddons.common.webhook.moka.handler.saveAndPublishEvent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage.NewItem;
import com.mekari.mokaaddons.common.webhook.moka.AbstractMokaEvent;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import lombok.Builder;

public class SaveAndPublishEvent extends AbstractVoidRequestHandler<SaveAndPublishEventRequest> {

    private @Autowired EventSourceStorage eventStorage;
    private @Autowired AmqpTemplate amqpTemplate;
    private @Autowired ObjectMapper mapper;

    private final String publishToExchangeName;

    public SaveAndPublishEvent(String publishToExchangeName) {
        Assert.notNull(publishToExchangeName, "publishToExchangeName must not be null");
        Assert.isTrue(publishToExchangeName.trim().length() != 0, "publishToExchangeName must not be empty");
        this.publishToExchangeName = publishToExchangeName;
    }

    public SaveAndPublishEvent(Config config) {
        Assert.notNull(config, "config must not be null");
        this.publishToExchangeName = config.publishToExchangeName;
        this.eventStorage = config.eventStorage;
        this.amqpTemplate = config.amqpTemplate;
        this.mapper = config.mapper;
    }

    @Override
    protected void handleInternal(SaveAndPublishEventRequest request) throws Exception {
        var event = request.getEvent();
        saveEvent(event);
        publishEvent(event);
    }

    protected void saveEvent(AbstractMokaEvent event) throws Exception {
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

    protected void publishEvent(AbstractMokaEvent event) {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.info(
                "publish webHookEventReceived eventId:%s-eventName:%s-dataId:%s with eventDate:%s into Queue:%sQueue",
                header.getEventId(), header.getEventName(), data.getId(), header.getTimestamp().toString(),
                publishToExchangeName);

        amqpTemplate.convertAndSend(publishToExchangeName, null, event);
    }

    @Builder
    public static class Config {
        public final String publishToExchangeName;
        public final EventSourceStorage eventStorage;
        public final AmqpTemplate amqpTemplate;
        public final ObjectMapper mapper;

        public Config(String publishToExchangeName
                , EventSourceStorage eventStorage
                , AmqpTemplate amqpTemplate
                , ObjectMapper mapper) {
            Assert.notNull(publishToExchangeName, "publishToExchangeName must not be null");
            Assert.isTrue(publishToExchangeName.trim().length() != 0, "publishToExchangeName must not be empty");
            Assert.notNull(eventStorage, "eventStorage must not be null");
            Assert.notNull(amqpTemplate, "amqpTemplate must not be null");
            Assert.notNull(mapper, "mapper must not be null");

            this.publishToExchangeName = publishToExchangeName.trim();
            this.eventStorage = eventStorage;
            this.amqpTemplate = amqpTemplate;
            this.mapper = mapper;
        }
    }
}
