package com.mekari.mokaaddons.webhookconsumer.webhook.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.common.handler.RequestHandlerManager;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.mekari.mokaaddons.webhookconsumer.webhook.event.MokaItemProcessedEvent;
import com.mekari.mokaaddons.webhookconsumer.webhook.service.item.processed.ProcessMokaItemProcessedRequest;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Consumer with manual event handle
 */
@Component
public class MokaItemProcessedConsumer {

    private @Autowired ObjectMapper mapper;
    private @Autowired RequestHandlerManager requestManager;
    private final static Logger LOGGER = LogManager.getFormatterLogger(MokaItemProcessedConsumer.class);

    @RabbitListener(queues = AppConstant.QueueName.MOKA_EVENT_PROCESSED_QUEUE)
    public void consume(Message message, Channel channel) throws Exception {
        var json = new String(message.getBody());
        LOGGER.debug("consume message:%s", json);

        var event = mapper.readValue(json, MokaItemProcessedEvent.class);
        var request = ProcessMokaItemProcessedRequest.builder()
                .event(event)
                .build();
        requestManager.handle(request);
    }
}

/*
    in kafka case, whether we consume from same topic with different consumer groups.
    
    //saving to db
    @KafkaListener(topic = "MokaIremReceived")
    public void consume(Message message, Channel channel) throws Exception {
        var json = new String(message.getBody());
        var event = mapper.readValue(json, MokaItemReceivedEvent.class);
        var request = MokaItemReceivedRequest.builder()
                .event(event)
                .build();
        requestManager.handle(request);
    }

    //sending email
    @KafkaListener(topic = "MokaIremReceived")
    public void consume(Message message, Channel channel) throws Exception {
        var json = new String(message.getBody());
        var event = mapper.readValue(json, MokaItemReceivedEvent.class);
        var request = SendEmailRequest.builder()
                .recipients(recipients)
                .bcc(bcc)
                .subject(subject)
                .body(event)
                .build();
        requestManager.handle(request);
    }

    //sending log
    @KafkaListener(topic = "MokaIremReceived")
    public void consume(Message message, Channel channel) throws Exception {
        var json = new String(message.getBody());
        var event = mapper.readValue(json, MokaItemReceivedEvent.class);
        var request = SendLogRequest.builder()
                .log(event)
                .build();
        requestManager.handle(request);
    }

    //sending event to flock
    @KafkaListener(topic = "MokaIremReceived")
    public void consume(Message message, Channel channel) throws Exception {
        var json = new String(message.getBody());
        var event = mapper.readValue(json, MokaItemReceivedEvent.class);
        var request = SendToFlockRequest.builder()
                .channel(channel)
                .event(event)
                .build();
        requestManager.handle(request);
    }
    */