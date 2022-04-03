package com.mekari.mokaaddons.webhookconsumer.service.publisher;

import com.mekari.mokaaddons.common.handler.AbstractVoidRequestHandler;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmqpPublishEvent extends AbstractVoidRequestHandler<PublishEventRequest>{ 
    private @Autowired AmqpTemplate amqpTemplate;

    @Override
    protected void handleInternal(PublishEventRequest request) {
        var routingKey = request.getProperties().get("routingKey");
        amqpTemplate.convertAndSend(request.getTopic(), routingKey, request.getMessage());
    }
}
