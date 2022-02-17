package com.mekari.mokaaddons.webhookhandler.consumer;

import com.mekari.mokaaddons.webhookhandler.common.consumer.AbstractConsumer;
import com.mekari.mokaaddons.webhookhandler.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MokaEventReceivedConsumer extends AbstractConsumer {

    @Override
    @RabbitListener(queues = AppConstant.QueueName.MOKA_EVENT_RECEIVED_QUEUE)
    public void consume(Message message, Channel channel) throws Exception{
        getLogger().info("received event :" + new String(message.getBody()));
        super.consume(message, channel);
    }
}