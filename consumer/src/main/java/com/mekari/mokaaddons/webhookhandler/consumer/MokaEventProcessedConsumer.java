package com.mekari.mokaaddons.webhookhandler.consumer;

import com.mekari.mokaaddons.webhookhandler.common.command.Invoker;
import com.mekari.mokaaddons.webhookhandler.common.consumer.AbstractConsumer;
import com.mekari.mokaaddons.webhookhandler.config.AppConstant;
import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MokaEventProcessedConsumer extends AbstractConsumer {

    public MokaEventProcessedConsumer(@Autowired Invoker invoker) {
        super(invoker);
    }

    @Override
    @RabbitListener(queues = AppConstant.QueueName.MOKA_EVENT_PROCESSED_QUEUE, concurrency = "1")
    public void consume(Message message, Channel channel) throws Exception{
        super.consume(message, channel);
    }
}
