package com.mekari.mokaaddons.webhookconsumer.webhook.consumer;

import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.common.webhook.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.consumer.AbstractEventMapConsumer;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.event.comparedate.CompareEventDateRequest;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Sample consumer inherited from AbstractEventMappedConsumer, which handles all events by leveraging configured event name class and request mapping through EventNameClassMap
 * see EventNameClassMap class which serves as mapper.
 * see WebhookConfig to know how to map them using EventNameClassMap. 
 */
@Component
public class MokaEventConsumer extends AbstractEventMapConsumer {

    private @Autowired RequestHandlerManager handlerManager;

    public MokaEventConsumer(@Qualifier("save.publish.event") EventNameClassMap eventClassMap) {
        super(eventClassMap);
    }

    @RabbitListener(queues = AppConstant.QueueName.MOKA_EVENT_RECEIVED_QUEUE)
    public void consume(Message message, Channel channel) throws Exception {
        var json = new String(message.getBody());

        consume(json, event->{
            //event date compares here.
            var compareDateRequest = CompareEventDateRequest.builder()
                                        .dataId(event.getBody().getData().getId().toString())
                                        .evenDate(event.getHeader().getTimestamp())
                                        .build();
            return handlerManager.handle(compareDateRequest).isEqualsWithLastEventDate();
        });
    }
}