package com.mekari.mokaaddons.webhookhandler.consumer;

import com.mekari.mokaaddons.webhookhandler.common.consumer.AbstractDeadLetterConsumer;
import com.mekari.mokaaddons.webhookhandler.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MokaDeadLetterConsumer extends AbstractDeadLetterConsumer {
    protected MokaDeadLetterConsumer(@Autowired Config config) {
        super(config);
    }

    private static final Logger LOGGER = LogManager.getFormatterLogger(MokaDeadLetterConsumer.class);

    @RabbitListener(queues = AppConstant.QueueName.MOKA_DLQ, concurrency = "1")
    public void consume(Message message, Channel channel) {
        try {
            super.consume(message, channel);
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }
}
