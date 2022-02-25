package com.mekari.mokaaddons.webhookhandler.common.consumer;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.storage.DeadLetterStorage;
import com.mekari.mokaaddons.webhookhandler.common.util.DateUtil;
import com.mekari.mokaaddons.webhookhandler.common.util.BuilderUtil;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import lombok.Builder;

public class AbstractDeadLetterConsumer {

    private @Autowired Config config;
    private String sourceName;
    private Logger logger;

    private static final String HEADER_X_RETRIES_COUNT = "x-retries-count";
    private static final String HEADER_X_DEATH_QUEUE = "x-first-death-queue";

    /**
     * do not use this default constructor, using another parameterized constructors
     * for manual instantiation instead.
     * this constuctor is neccessary by springboot to instantiate this class.
     */
    protected AbstractDeadLetterConsumer() {
        init();
    }

    protected AbstractDeadLetterConsumer(Config config) {
        Assert.notNull(config, "config must not be null");
        this.config = config;
        init();
    }

    protected void init() {
        sourceName = this.getClass().getName();
        logger = LogManager.getFormatterLogger(this.getClass());
    }

    protected final String getSourceName() {
        return sourceName;
    }

    protected final Logger getLogger() {
        return logger;
    }

    public void consume(Message message, Channel channel) {
        try {
            var header = message.getMessageProperties().getHeaders();
            var attempt = (Integer) header.get(HEADER_X_RETRIES_COUNT);

            if (attempt == null)
                attempt = 1;
            if (attempt > config.maxRetriesCount) {
                logger.info("Discarding message and save %s into dead_letter storage", new String(message.getBody()));
                saveDeadLetter(message, /* temporary */ "rejected");
                return;
            }

            var exchangeRoutingKey = config.exchangeRoutingStrategy.get(message);
            header.put(HEADER_X_RETRIES_COUNT, ++attempt);
            config.amqpTemplate.send(exchangeRoutingKey.exchangeName, exchangeRoutingKey.routingKey, message);
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
            msgNode = config.mapper.readTree(msg);
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
                config.deadLetterStorage.insert(builder.build());
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

            if(exchangeRoutingStrategy != null) 
                this.exchangeRoutingStrategy = exchangeRoutingStrategy;
            else this.exchangeRoutingStrategy =  AbstractDeadLetterConsumer::getExchangeRoutingKey;
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
