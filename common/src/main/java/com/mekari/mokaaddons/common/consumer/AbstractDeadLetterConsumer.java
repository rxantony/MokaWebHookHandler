package com.mekari.mokaaddons.common.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.util.DateUtil;
import com.mekari.mokaaddons.common.webhook.BuilderUtil;
import com.mekari.mokaaddons.common.webhook.DeadLetterStorage;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import lombok.Builder;

public class AbstractDeadLetterConsumer {

    private @Autowired ObjectMapper mapper;
    private @Autowired AmqpTemplate amqpTemplate;
    private @Autowired DeadLetterStorage deadLetterStorage;
    private @Autowired(required = false) ExchangeRoutingStrategy exchangeRoutingStrategy;
    private int maxRetriesCount = 4;
    private String sourceName;
    private Logger logger;

    private static final String HEADER_X_RETRIES_COUNT = "x-retries-count";
    private static final String HEADER_X_DEATH_QUEUE = "x-first-death-queue";

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary for springboot to instantiate this class.
     */
    protected AbstractDeadLetterConsumer() {
        init();
    }

    protected AbstractDeadLetterConsumer(int maxRetriesCount) {
        this.maxRetriesCount = maxRetriesCount;
        init();
    }

    protected AbstractDeadLetterConsumer(Config config) {
        Assert.notNull(config, "config must not be null");

        this.deadLetterStorage = config.deadLetterStorage;
        this.amqpTemplate = config.amqpTemplate;
        this.mapper = config.mapper;
        this.maxRetriesCount = config.maxRetriesCount;
        this.exchangeRoutingStrategy = config.exchangeRoutingStrategy;
        init();
    }

    protected void init() {
        if(exchangeRoutingStrategy == null) 
            this.exchangeRoutingStrategy =  AbstractDeadLetterConsumer::getExchangeRoutingKey;
        sourceName = this.getClass().getName();
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    protected final Logger getLogger() {
        return logger;
    }

    public void consume(Message message, Channel channel) {
        try {
            var headers = message.getMessageProperties().getHeaders();
            var attempt = (Integer) headers.get(HEADER_X_RETRIES_COUNT);

            if (attempt == null)
                attempt = 1;
            if (attempt > maxRetriesCount) {
                logger.info("Discarding message and save %s into dead_letter storage", new String(message.getBody()));
                saveDeadLetter(message, /* temporary */ "rejected");
                return;
            }

            var exchangeRoutingKey = exchangeRoutingStrategy.get(message);
            headers.put(HEADER_X_RETRIES_COUNT, ++attempt);
            amqpTemplate.send(exchangeRoutingKey.exchangeName, exchangeRoutingKey.routingKey, message);
        } catch (Exception ex) {
            logger.error(ex.toString());
            saveDeadLetter(message, ex.toString());
        }
    }

    private static ExchangeRoutingStrategy.Result getExchangeRoutingKey(Message message) {
        var headers = message.getMessageProperties().getHeaders();
        var toQueue = (String) headers.get(HEADER_X_DEATH_QUEUE);
        var exchange = toQueue.substring(0, toQueue.length() - "Queue".length());
        var routingKey = message.getMessageProperties().getReceivedRoutingKey();
        return new ExchangeRoutingStrategy.Result(exchange, routingKey);
    }

    protected void saveDeadLetter(Message message, String reason) {
        var msg = new String(message.getBody());
        JsonNode msgNode = null;
        try {
            msgNode = mapper.readTree(msg);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        finally{
            try{
                var builder = BuilderUtil.createDeadLetterStorageItemBuilder(msgNode)
                    .source(sourceName)
                    .payload(msg)
                    .properties(message.getMessageProperties().toString())
                    .reason(reason)
                    .createdAt(DateUtil.now());
                deadLetterStorage.insert(builder.build());
            }
            catch(Exception ex){
                logger.error(ex.toString());
            }
        }
    }

    @Builder
    public static class Config {
        public final DeadLetterStorage deadLetterStorage;
        public final AmqpTemplate amqpTemplate;
        public final ObjectMapper mapper;
        public final int maxRetriesCount;
        public final ExchangeRoutingStrategy exchangeRoutingStrategy;

        public Config(DeadLetterStorage deadLetterStorage, AmqpTemplate amqpTemplate, ObjectMapper mapper,
            int maxRetriesCount, ExchangeRoutingStrategy exchangeRoutingStrategy) {
            Assert.notNull(deadLetterStorage, "deadLetterStorage must not be null");
            Assert.notNull(amqpTemplate, "amqpTemplate must not be null");
            Assert.notNull(mapper, "mapper must not be null");
            Assert.isTrue(maxRetriesCount > 0, "maxRetriesCount must not be lower than 1");

            this.deadLetterStorage = deadLetterStorage;
            this.amqpTemplate = amqpTemplate;
            this.mapper = mapper;
            this.maxRetriesCount = maxRetriesCount;
            this.exchangeRoutingStrategy = exchangeRoutingStrategy;
        }
    }

    public static interface ExchangeRoutingStrategy{
        Result get(Message message);

        public static class Result{
            public final String exchangeName;
            public final String routingKey;
    
            public Result(String exchangeName, String routingKey){
                this.exchangeName = exchangeName;
                this.routingKey = routingKey;
            }
        }
    }
}
