package com.mekari.mokaaddons.webhookconsumer.consumer;

import com.mekari.mokaaddons.common.consumer.AbstractRabbitMQDeadLetterConsumer;
import com.mekari.mokaaddons.webhookconsumer.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MokaDeadLetterConsumer extends AbstractRabbitMQDeadLetterConsumer {

    @RabbitListener(queues = AppConstant.QueueName.MOKA_DLQ)
    public void consume(Message message, Channel channel) {
        super.consume(message, channel);
    }
}
