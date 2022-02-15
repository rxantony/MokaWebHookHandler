package com.mekari.mokaaddons.webhookhandler.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.webhookhandler.common.consumer.AbstractConsumer;
import com.mekari.mokaaddons.webhookhandler.common.processor.EventProcessorManager;
import com.mekari.mokaaddons.webhookhandler.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MokaEventProcessedConsumer extends AbstractConsumer {

    public MokaEventProcessedConsumer(@Autowired EventProcessorManager eventProcessorManager
        , @Autowired ObjectMapper mapper) {
        super(eventProcessorManager, mapper);
    }

    @Override
    @RabbitListener(queues = AppConstant.QueueName.MOKA_EVENT_PROCESSED_QUEUE, concurrency = "1")
    public void consume(Message message, Channel channel) throws Exception{
        super.consume(message, channel);
    }
}
