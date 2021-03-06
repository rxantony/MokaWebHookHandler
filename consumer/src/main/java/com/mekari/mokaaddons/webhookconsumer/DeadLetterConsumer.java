package com.mekari.mokaaddons.webhookconsumer;

import com.mekari.mokaaddons.common.messaging.rabbitmq.AbstractRabbitMQDLConsumer;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterConsumer extends AbstractRabbitMQDLConsumer {

    @Override
    @RabbitListener(queues = AppConstant.QueueName.MOKA_DLQ)
    public void consume(Message message, Channel channel) {
        super.consume(message, channel);
    }
}
