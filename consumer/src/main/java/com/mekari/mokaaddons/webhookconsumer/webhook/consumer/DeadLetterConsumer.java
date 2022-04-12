package com.mekari.mokaaddons.webhookconsumer.webhook.consumer;

import com.mekari.mokaaddons.common.infrastructure.messaging.rabbitmq.AbstractRabbitMQDLConsumer;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterConsumer extends AbstractRabbitMQDLConsumer {

    @RabbitListener(queues = AppConstant.QueueName.MOKA_DLQ)
    public void consume(Message message, Channel channel) {
        super.consume(message, channel);
    }
}
