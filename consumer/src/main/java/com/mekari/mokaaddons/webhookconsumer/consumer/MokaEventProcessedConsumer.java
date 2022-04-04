package com.mekari.mokaaddons.webhookconsumer.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemProcessed.MokaItemProcessedEvent;
import com.mekari.mokaaddons.webhookconsumer.service.webhook.mokaItemProcessed.MokaItemProcessedRequest;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MokaEventProcessedConsumer {

    private @Autowired ObjectMapper mapper;
    private @Autowired RequestHandlerManager requestManager;
    private final static Logger LOGGER = LogManager.getFormatterLogger(MokaEventProcessedConsumer.class);

    @RabbitListener(queues = AppConstant.QueueName.MOKA_EVENT_PROCESSED_QUEUE)
    public void consume(Message message, Channel channel) throws Exception {
        var json = new String(message.getBody());
        var event = mapper.readValue(json, MokaItemProcessedEvent.class);
        var request = MokaItemProcessedRequest.builder()
                .event(event)
                .build();
        requestManager.handle(request);
    }
}
