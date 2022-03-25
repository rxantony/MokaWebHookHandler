package com.mekari.mokaaddons.webhookhandler.common.command.moka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.command.AbstractCommand;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.AbstractMokaEvent;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage.NewItem;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import lombok.Builder;

public class SaveToDbAndPublishCommand<TEvent extends AbstractMokaEvent> extends AbstractCommand<TEvent> {

    private @Autowired EventSourceStorage eventStorage;
    private @Autowired AmqpTemplate amqpTemplate;
    private @Autowired ObjectMapper mapper;

    private final String publishToExchangeName;

    public SaveToDbAndPublishCommand(String publishToExchangeName, Class<TEvent> eventCls) {
        super(eventCls);
        Assert.notNull(publishToExchangeName, "publishToExchangeName must not be null");
        Assert.isTrue(publishToExchangeName.trim().length() != 0, "publishToExchangeName must not be empty");
        this.publishToExchangeName = publishToExchangeName;
    }

    public SaveToDbAndPublishCommand(Config config, Class<TEvent> eventCls) {
        super(eventCls);
        Assert.notNull(config, "config must not be null");
        this.publishToExchangeName = config.publishToExchangeName;
        this.eventStorage = config.eventStorage;
        this.amqpTemplate = config.amqpTemplate;
        this.mapper = config.mapper;
    }

    @Override
    protected void executeInternal(TEvent event) throws Exception {
        saveEvent(event);
        publishEvent(event);
    }

    protected void saveEvent(TEvent event) throws Exception {
        var header = event.getHeader();
        var data = event.getBody().getData();

        logger.info("insert event into even_store eventId:%s-eventName:%s-dataId:%s with updatedAt:%s",
                header.getEventId(), header.getEventName(), data.getId(), header.getTimestamp().toString());

        eventStorage
            .insert(NewItem
                    .builder()
                    .dataId(data.getId())
                    .eventDate(event.getDate())
                    .eventName(header.getEventName())
                    .payload(mapper.writeValueAsString(event))
                    .eventId(header.getEventId())
                    .outletId(header.getOutletId())
                    .version(header.getVersion())
                    .timestamp(header.getTimestamp())
                    .createdAt(DateUtil.now())
                    .build());
    }

    protected void publishEvent(TEvent event) {
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