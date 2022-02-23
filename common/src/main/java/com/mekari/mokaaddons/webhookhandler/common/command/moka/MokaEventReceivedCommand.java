package com.mekari.mokaaddons.webhookhandler.common.command.moka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.command.AbstractEventCommand;
import com.mekari.mokaaddons.webhookhandler.common.event.moka.AbstractMokaEvent;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.EventSourceStorage.NewItem;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.Assert;

public class MokaEventReceivedCommand<TEvent extends AbstractMokaEvent<?>> extends AbstractEventCommand<TEvent> {

    private final Config config;

    public MokaEventReceivedCommand(Config config, Class<TEvent> eventCls) {
        super(eventCls);
        Assert.notNull(config, "config must not be null");
        this.config = config;
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

        config.eventStorage
                .insert(NewItem
                        .builder()
                        .dataId(data.getId())
                        .eventDate(header.getTimestamp())
                        .eventName(header.getEventName())
                        .payload(config.mapper.writeValueAsString(event))
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
                config.exchangeName);

        config.amqpTemplate.convertAndSend(config.exchangeName, null, event);
    }

    public static class Config {
        public final String exchangeName;
        public final ObjectMapper mapper;
        public final EventSourceStorage eventStorage;
        public final AmqpTemplate amqpTemplate;

        public Config(String exchangeName, EventSourceStorage eventStorage, AmqpTemplate amqpTemplate,
                ObjectMapper mapper) {
            Assert.notNull(exchangeName, "exchangeName must not be null");
            Assert.isTrue(exchangeName.trim().length() != 0, "exchangeName must not be empty");
            Assert.notNull(eventStorage, "eventStorage must not be null");
            Assert.notNull(amqpTemplate, "amqpTemplate must not be null");
            Assert.notNull(mapper, "mapper must not be null");

            this.exchangeName = exchangeName.trim();
            this.eventStorage = eventStorage;
            this.amqpTemplate = amqpTemplate;
            this.mapper = mapper;
        }
    }
}