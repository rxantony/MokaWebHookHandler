package com.mekari.mokaaddons.webhookhandler.consumer;

import com.mekari.mokaaddons.webhookhandler.common.consumer.AbstractDeadLetterConsumer;
import com.mekari.mokaaddons.webhookhandler.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MokaDeadLetterConsumer extends AbstractDeadLetterConsumer {

    @RabbitListener(queues = AppConstant.QueueName.MOKA_DLQ)
    public void consume(Message message, Channel channel) {
        super.consume(message, channel);
    }
}
