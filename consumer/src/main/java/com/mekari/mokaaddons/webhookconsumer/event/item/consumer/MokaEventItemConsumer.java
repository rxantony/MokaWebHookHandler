package com.mekari.mokaaddons.webhookconsumer.event.item.consumer;

import com.mekari.mokaaddons.common.webhook.event.EventNameClassMap;
import com.mekari.mokaaddons.common.webhook.event.moka.EventRequest;
import com.mekari.mokaaddons.common.webhook.event.AbstractEventNameClassMapConsumer;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.mekari.mokaaddons.webhookconsumer.service.event.command.comparedate.CompareEventDateRequest;
import com.mekari.mokaaddons.webhookconsumer.service.event.command.lock.LockEventRequest;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Sample consumer inherited from AbstractEventMappedConsumer, which handles all
 * events by leveraging configured event name class and request mapping through
 * EventNameClassMap
 * see EventNameClassMap class which serves as mapper.
 * see WebhookConfig to know how to map them using EventNameClassMap.
 */
@Component
public class MokaEventItemConsumer extends AbstractEventNameClassMapConsumer {

    public MokaEventItemConsumer(@Qualifier("moka.item.event") EventNameClassMap eventClassMap) {
        super(eventClassMap);
    }

    @RabbitListener(queues = AppConstant.QueueName.MOKA_EVENT_RECEIVED_QUEUE)
    public void consume(Message message, Channel channel) throws Exception {
        var json = new String(message.getBody());
        handle(json, request -> {
            /*
                event lock here
            */
            var evRequest = (EventRequest) request;
            var lockRequest = LockEventRequest.builder().event(evRequest.getEvent()).build();
            
            try (var lockResult = getHandlerManager().handle(lockRequest);) {
                /*
                    event date compares here
                */
                var compareDateRequest = CompareEventDateRequest.builder()
                        .event(evRequest.getEvent())
                        .build();

                if (!getHandlerManager().handle(compareDateRequest).isEqualsWithLastEventDate()) {
                    return;
                }

                getHandlerManager().handle(request);
            }
        });
    }
}