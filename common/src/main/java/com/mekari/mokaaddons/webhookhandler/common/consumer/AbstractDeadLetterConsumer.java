package com.mekari.mokaaddons.webhookhandler.common.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage.DeadLeterItem;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.util.Assert;

public class AbstractDeadLetterConsumer {

    private String sourceName;
    private final Config config;
    protected final Logger logger;

    private static final String HEADER_X_RETRIES_COUNT = "x-retries-count";
    private static final String HEADER_X_DEATH_QUEUE = "x-first-death-queue";

    protected AbstractDeadLetterConsumer(Config config){
        Assert.notNull(config, "config must not be null");

        this.config = config;
        sourceName = this.getClass().getName();
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    public void consume(Message message, Channel channel) throws Exception{
        try {
            var header = message.getMessageProperties().getHeaders();
            var retriesCnt = (Integer) header.get(HEADER_X_RETRIES_COUNT);
            if (retriesCnt == null) retriesCnt = 1;
            if (retriesCnt > config.maxRetriesCount) {
                saveFailedMessage(message);
                logger.info("Discarding and save message %s into dead_letter storage", new String(message.getBody()));
                return;
            }
            var toQueue = (String)header.get(HEADER_X_DEATH_QUEUE);
            var toExchange = toQueue.substring(0, toQueue.length() - "Queue".length());

            var toRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
            toRoutingKey = toExchange==null||toExchange.length() == 0 ? toRoutingKey : toRoutingKey;

            header.put(HEADER_X_RETRIES_COUNT, ++retriesCnt);
            config.amqpTemplate.send(toExchange, toRoutingKey, message);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    protected void saveFailedMessage(Message message) throws Exception{
        var msg = new String(message.getBody());
        var itemBuilder = DeadLeterItem.builder()
                .source(sourceName)
                .payload(msg)
                .properties(message.getMessageProperties().toString())
                .createdAt(DateUtil.now());
        var msgNode = config.mapper.readTree(msg);
        var header = msgNode.get("header");
        if(header != null){
            var event_id = header.get("event_id");
            if(event_id != null)
                itemBuilder.eventId(event_id.asText());
        }
        config.deadLetterStorage.insert(itemBuilder.build());
    }

    public static class Config {
        public final DeadLetterStorage deadLetterStorage;
        public final AmqpTemplate amqpTemplate;
        public final ObjectMapper mapper;
        public final int maxRetriesCount;

        public Config(DeadLetterStorage deadLetterStorage, AmqpTemplate amqpTemplate, ObjectMapper mapper,
                int maxRetriesCount) {
                Assert.notNull(deadLetterStorage, "deadLetterStorage must not be null");
                Assert.notNull(amqpTemplate, "amqpTemplate must not be null");
                Assert.notNull(mapper, "mapper must not be null");
                Assert.isTrue(maxRetriesCount > 0, "maxRetriesCount must not be lower than 1");
        
                this.deadLetterStorage = deadLetterStorage;
                this.amqpTemplate = amqpTemplate;
                this.mapper = mapper;
                this.maxRetriesCount = maxRetriesCount;
        }
    }
}
