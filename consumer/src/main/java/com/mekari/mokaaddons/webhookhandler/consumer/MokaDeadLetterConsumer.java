package com.mekari.mokaaddons.webhookhandler.consumer;

import com.mekari.mokaaddons.webhookhandler.common.consumer.AbstractDeadLetterConsumer;
import com.mekari.mokaaddons.webhookhandler.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MokaDeadLetterConsumer extends AbstractDeadLetterConsumer {
    protected MokaDeadLetterConsumer(@Autowired Config config) {
        super(config);
    }

    @RabbitListener(queues = AppConstant.QueueName.MOKA_DLQ, concurrency = "1")
    public void consume(Message message, Channel channel) {
        try {
            super.consume(message, channel);
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }
}
