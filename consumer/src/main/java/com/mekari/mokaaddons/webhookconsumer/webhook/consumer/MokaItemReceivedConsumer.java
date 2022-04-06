package com.mekari.mokaaddons.webhookconsumer.webhook.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.webhook.EventNameClassMap;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sample consumer which handles all events by leveraging configured event name class and request mapping through EventNameClassMap
 * see EventNameClassMap class which serves as mapper.
 * see WebhookConfig to know how to map them using EventNameClassMap. 
 */
@Component
public class MokaItemReceivedConsumer {

    private @Autowired ObjectMapper mapper;
    private @Autowired EventNameClassMap eventClsMap;
    private @Autowired RequestHandlerManager requestManager;
    private final static Logger LOGGER = LogManager.getFormatterLogger(MokaItemReceivedConsumer.class);

    @RabbitListener(queues = AppConstant.QueueName.MOKA_EVENT_RECEIVED_QUEUE)
    public void consume(Message message, Channel channel) throws Exception {
        var json = new String(message.getBody());
        LOGGER.debug("consume message:%s", json);
        
        var jsonNode = mapper.readTree(json);
        var eventName = jsonNode.get("header").get("event_name").asText();
        var mapItem = eventClsMap.get(eventName);
        var event = mapper.readValue(jsonNode.traverse(), mapItem.eventClass);
        var request = mapItem.requestFactory.apply(event);
        requestManager.handle(request);
    }
}