package com.mekari.mokaaddons.webhookconsumer.consumer;

import com.mekari.mokaaddons.common.consumer.AbstractConsumer;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MokaEventReceivedConsumer extends AbstractConsumer {

    @Override
    @RabbitListener(queues = AppConstant.QueueName.MOKA_EVENT_RECEIVED_QUEUE)
    public void consume(Message message, Channel channel) throws Exception{
        super.consume(message, channel);
    }
}