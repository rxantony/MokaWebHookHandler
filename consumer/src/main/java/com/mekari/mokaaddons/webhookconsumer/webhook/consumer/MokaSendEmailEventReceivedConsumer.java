package com.mekari.mokaaddons.webhookconsumer.webhook.consumer;

import com.mekari.mokaaddons.common.webhook.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.consumer.AbstractEventMapConsumer;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Sample consumer inherited from AbstractEventMappedConsumer, which handles all events by leveraging configured event name class and request mapping through EventNameClassMap
 * see EventNameClassMap class which serves as mapper.
 * see WebhookConfig to know how to map them using EventNameClassMap. 
 */
@Component
public class MokaSendEmailEventReceivedConsumer extends AbstractEventMapConsumer {

    public MokaSendEmailEventReceivedConsumer(@Qualifier("send.email") EventNameClassMap eventClassMap) {
        super(eventClassMap);
    }

    @RabbitListener(queues = AppConstant.QueueName.MOKA_EVENT_RECEIVED_QUEUE)
    public void consume(Message message, Channel channel) throws Exception {
        var json = new String(message.getBody());
        consume(json);
    }
}