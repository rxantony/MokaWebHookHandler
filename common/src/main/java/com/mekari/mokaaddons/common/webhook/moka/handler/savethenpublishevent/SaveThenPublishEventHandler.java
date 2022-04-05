package com.mekari.mokaaddons.common.webhook.moka.handler.savethenpublishevent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;
import com.mekari.mokaaddons.common.infrastructure.messaging.Publisher;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage;
import com.mekari.mokaaddons.common.webhook.EventSourceStorage.NewItem;
import com.mekari.mokaaddons.common.webhook.moka.AbstractEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import lombok.Builder;

public class SaveThenPublishEventHandler extends AbstractVoidRequestHandler<SaveThenPublishEventRequest> {

    private @Autowired EventSourceStorage eventStorage;
    private @Autowired Publisher publisher;
    private @Autowired ObjectMapper mapper;

    private final String publishToExchangeName;

    public SaveThenPublishEventHandler(String publishToExchangeName) {
        Assert.notNull(publishToExchangeName, "publishToExchangeName must not be null");
        Assert.isTrue(publishToExchangeName.trim().length() != 0, "publishToExchangeName must not be empty");
        this.publishToExchangeName = publishToExchangeName;
    }

    public SaveThenPublishEventHandler(Config config) {
        Assert.notNull(config, "config must not be null");
        this.publishToExchangeName = config.publishToExchangeName;
        this.eventStorage = config.eventStorage;
        this.publisher = config.publisher;
        this.mapper = config.mapper;
    }

    @Override
    protected void handleInternal(SaveThenPublishEventRequest request) throws Exception {
        var event = request.getEvent();
        saveEvent(event);
        publishEvent(event);
    }

    protected void saveEvent(AbstractEvent event) throws Exception {
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

    protected void publishEvent(AbstractEvent event) {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.info(
                "publish webHookEventReceived eventId:%s-eventName:%s-dataId:%s with eventDate:%s into Queue:%sQueue",
                header.getEventId(), header.getEventName(), data.getId(), header.getTimestamp().toString(),
                publishToExchangeName);

        publisher.publish(publishToExchangeName, event);
    }

    @Builder
    public static class Config {
        public final String publishToExchangeName;
        public final EventSourceStorage eventStorage;
        public final Publisher publisher;
        public final ObjectMapper mapper;

        public Config(String publishToExchangeName
                , EventSourceStorage eventStorage
                , Publisher publisher
                , ObjectMapper mapper) {
            Assert.notNull(publishToExchangeName, "publishToExchangeName must not be null");
            Assert.isTrue(publishToExchangeName.trim().length() != 0, "publishToExchangeName must not be empty");
            Assert.notNull(eventStorage, "eventStorage must not be null");
            Assert.notNull(publisher, "publisher must not be null");
            Assert.notNull(mapper, "mapper must not be null");

            this.publishToExchangeName = publishToExchangeName.trim();
            this.eventStorage = eventStorage;
            this.publisher = publisher;
            this.mapper = mapper;
        }
    }
}
