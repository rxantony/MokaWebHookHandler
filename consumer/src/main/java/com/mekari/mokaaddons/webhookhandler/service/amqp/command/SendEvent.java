package com.mekari.mokaaddons.webhookhandler.service.amqp.command;

import com.mekari.mokaaddons.webhookhandler.common.handler.AbstractVoidRequestHandler;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendEvent extends AbstractVoidRequestHandler<SendEventRequest>{ 
    private @Autowired AmqpTemplate amqpTemplate;

    @Override
    protected void handleInternal(SendEventRequest request) {
        amqpTemplate.convertAndSend(request.getExchange(), request.getRoutingKey(), request.getMessage());
    }
}
